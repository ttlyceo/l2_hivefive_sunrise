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
package l2r.gameserver.model.conditions;

import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.instancemanager.CastleManager;
import l2r.gameserver.instancemanager.FortManager;
import l2r.gameserver.instancemanager.TerritoryWarManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Castle;
import l2r.gameserver.model.entity.Fort;
import l2r.gameserver.model.stats.Env;
import l2r.gameserver.network.SystemMessageId;

/**
 * Player Can Create Outpost condition implementation.
 * @author Adry_85
 */
public class ConditionPlayerCanCreateOutpost extends Condition
{
	private final boolean _val;
	
	public ConditionPlayerCanCreateOutpost(boolean val)
	{
		_val = val;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		boolean canCreateOutpost = true;
		if ((env.getPlayer() == null) || env.getPlayer().isAlikeDead() || env.getPlayer().isCursedWeaponEquipped() || (env.getPlayer().getClan() == null))
		{
			canCreateOutpost = false;
		}
		
		final Castle castle = CastleManager.getInstance().getCastle(env.getPlayer());
		final Fort fort = FortManager.getInstance().getFort(env.getPlayer());
		
		if ((castle == null) && (fort == null))
		{
			canCreateOutpost = false;
		}
		
		L2PcInstance player = env.getPlayer().getActingPlayer();
		if (((fort != null) && (fort.getResidenceId() == 0)) || ((castle != null) && (castle.getResidenceId() == 0)))
		{
			player.sendMessage("您必須在堡壘或城堡地面上才能建造前哨站或旗幟。");
			canCreateOutpost = false;
		}
		else if (((fort != null) && !fort.getZone().isActive()) || ((castle != null) && !castle.getZone().isActive()))
		{
			player.sendMessage("您只能在攻城戰場上建造前哨站或旗幟。");
			canCreateOutpost = false;
		}
		else if (!player.isClanLeader())
		{
			player.sendMessage("您必須是血盟盟主才能建造前哨站或旗幟。");
			canCreateOutpost = false;
		}
		else if (TerritoryWarManager.getInstance().getHQForClan(player.getClan()) != null)
		{
			player.sendPacket(SystemMessageId.NOT_ANOTHER_HEADQUARTERS);
			canCreateOutpost = false;
		}
		else if (TerritoryWarManager.getInstance().getFlagForClan(player.getClan()) != null)
		{
			player.sendPacket(SystemMessageId.A_FLAG_IS_ALREADY_BEING_DISPLAYED_ANOTHER_FLAG_CANNOT_BE_DISPLAYED);
			canCreateOutpost = false;
		}
		else if (!player.isInsideZone(ZoneIdType.HQ))
		{
			player.sendPacket(SystemMessageId.NOT_SET_UP_BASE_HERE);
			canCreateOutpost = false;
		}
		return (_val == canCreateOutpost);
	}
}