/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2r.gameserver.network.clientpackets;

import java.nio.BufferUnderflowException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import l2r.gameserver.network.L2GameClient;
import l2r.gameserver.network.serverpackets.KeyPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jsoft.jguard.JGuard;
import top.jsoft.jguard.JGuardConfig;
import top.jsoft.jguard.manager.HWIDBanManager;

/**
 * Format: c dddd
 * @author KenM
 */
public class GameGuardReply extends L2GameClientPacket
{
	private static final String _C__CB_GAMEGUARDREPLY = "[C] CB GameGuardReply";

	private static final Logger LOGGER = LoggerFactory.getLogger(GameGuardReply.class);
	private static final int revisionNumber = JGuardConfig.JGUARD_REVISION_NUMBER;

	private int code;
	private int section;
	private int index;
	private int revision;
	private String procBuf;

	private static final byte[] VALID =
	{
		(byte) 0x88,
		0x40,
		0x1c,
		(byte) 0xa7,
		(byte) 0x83,
		0x42,
		(byte) 0xe9,
		0x15,
		(byte) 0xde,
		(byte) 0xc3,
		0x68,
		(byte) 0xf6,
		0x2d,
		0x23,
		(byte) 0xf1,
		0x3f,
		(byte) 0xee,
		0x68,
		0x5b,
		(byte) 0xc5,
	};
	
	private final byte[] _reply = new byte[8];
	
	@Override
	protected void readImpl()
	{
		if(JGuard.isProtectEnabled()) {
			code = readD();
			section = readD();
			index = readD();
			revision = readD();
			if (code == 0x01) {
				try {
					procBuf = readS();
				} catch (BufferUnderflowException e) {

				}
			}
		}
		else
		{
			readB(_reply, 0, 4);
			readD();
			readB(_reply, 4, 4);
		}
	}
	
	@Override
	protected void runImpl()
	{
		L2GameClient client = getClient();
		if (client == null) {
			return;
		}

		if(!JGuard.isProtectEnabled())
		{
			try
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				byte[] result = md.digest(_reply);
				if (Arrays.equals(result, VALID))
				{
					client.setGameGuardOk(true);
				}
			}
			catch (NoSuchAlgorithmException e)
			{
				_log.warn(String.valueOf(e));
			}
		}


		if(JGuardConfig.JGUARD_DEBUG)
			LOGGER.info(String.format("Account: %s, code(%d), section(%d), index(%d), revision(%d)", client.getLogin(), code, section, index, revision));

		if (revisionNumber != -1 && revision != revisionNumber) {
			LOGGER.warn("[JGuard] Client: " + client.getIPAddress() + " requested not correct revision from protect. Current revision: " + JGuardConfig.JGUARD_REVISION_NUMBER + " Client revision: " + revision);
			client.close(new KeyPacket(client.enableCrypt(), 0));
			return;
		}

		final JGuard.GameGuardResponse gg = JGuard.GameGuardResponse.values()[code];

		switch (gg) {
			case NONE:
				client.setGameGuardOk(false);
				LOGGER.warn("Game Guard response NONE. Check this. Maybe hucked client(bot, antiGuard, etc)");
				break;
			case NORMAL_RESPONSE:
				client.setGameGuardOk(true);
				//client.sendPacket(new NetPing(client.getAccountId()));
				if (procBuf != null && !procBuf.isEmpty()) {
					JGuard.getInstance().storeProcBufInfo(client.getLogin(), procBuf);
				}
				//if(client.getActiveChar() != null)
				//	client.sendPacket(new DataPacket(client.getActiveChar().getName(null)));
				break;
			case KICK_RESPONSE:
				client.setGameGuardOk(false);
				HWIDBanManager.getInstance().systemBan(client, JGuard.GameGuardResponse.KICK_RESPONSE.toString(), HWIDBanManager.BanType.NONE);
				break;
			case USED_BOT_RESPONS:
				client.setGameGuardOk(false);
				//final BotResponse bot = BotResponse.values()[section];
				//Client senede info from bot detection. Value in BotResponse enume. Use bot enum, and ban/kick him :)
				HWIDBanManager.getInstance().systemBan(client, JGuard.GameGuardResponse.USED_BOT_RESPONS.toString(), HWIDBanManager.BanType.PLAYER_BAN);
				break;
			case GET_SN_IS_FALSE_RESPONSE:
				client.setGameGuardOk(false);
				HWIDBanManager.getInstance().systemBan(client, JGuard.GameGuardResponse.GET_SN_IS_FALSE_RESPONSE.toString(), HWIDBanManager.BanType.ACCOUNT_BAN);
				break;
			case SN_NULL_LENGHT_RESPONSE:
				client.setGameGuardOk(false);
				HWIDBanManager.getInstance().systemBan(client, JGuard.GameGuardResponse.SN_NULL_LENGHT_RESPONSE.toString(), HWIDBanManager.BanType.ACCOUNT_BAN);
				break;
			case SP_OBJECT_CHANGED_RESPONSE:
				client.setGameGuardOk(false);
				LOGGER.warn("Game Guard response SP_OBJECT_CHANGED_RESPONSE. Check this. Maybe hucked client(bot, antiGuard, etc)");
				HWIDBanManager.getInstance().systemBan(client, JGuard.GameGuardResponse.SP_OBJECT_CHANGED_RESPONSE.toString(), HWIDBanManager.BanType.ACCOUNT_BAN);
				break;
			case REQUEST_REVISION_VALIDATE:
				client.setGameGuardOk(false);
				break;
			case NOT_VALID_HOST_INFO:
				client.setGameGuardOk(false);
				LOGGER.warn("[JGuard] Client: " + client.getRealIpAddress() + " changed host info.");
				client.close(new KeyPacket(client.enableCrypt(), 0));
				break;
		}
	}
	
	@Override
	public String getType()
	{
		return _C__CB_GAMEGUARDREPLY;
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}
