package gr.sr.spreeEngine;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.clientpackets.Say2;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.ExShowScreenMessage;
import l2r.gameserver.util.Broadcast;

/**
 * @author L2jSunrise Team
 * @Website www.l2jsunrise.com
 */
public class SpreeHandler
{
	public SpreeHandler()
	{
		// Dummy default
	}
	
	public void spreeSystem(L2PcInstance player, int spreeKills)
	{
		ExShowScreenMessage msgCase = null;
		String announceMessage = null;
		
		switch (spreeKills)
		{
			case 1:
				break;
			case 2:
				break;
			case 3:
				msgCase = new ExShowScreenMessage("你已達成 3 連殺！", 4000);
				announceMessage = "三連殺!";
				break;
			case 4:
				msgCase = new ExShowScreenMessage("你已達成 4 連殺！", 4000);
				announceMessage = "剛剛達成了四連殺!";
				break;
			case 5:
				msgCase = new ExShowScreenMessage("你已達成 5 連殺！", 4000);
				announceMessage = "剛剛達成了五連殺";
				break;
			case 8:
				msgCase = new ExShowScreenMessage("你已達成 8 連殺！", 4000);
				announceMessage = "已經無人能擋！";
				break;
			case 10:
				msgCase = new ExShowScreenMessage("你已達成 10 連殺！", 4000);
				announceMessage = "剛剛完成 十連殺";
				break;
			case 13:
				msgCase = new ExShowScreenMessage("你已達成 13 連殺！", 4000);
				announceMessage = "已經瘋狂殺戮!";
				break;
			case 15:
				msgCase = new ExShowScreenMessage("你已達成 15 連殺！", 4000);
				announceMessage = "正在進行屠宰!!!";
				break;
			case 20:
				msgCase = new ExShowScreenMessage("你已達成 20 連殺！", 4000);
				announceMessage = "正在主宰戰場!!";
				break;
			case 25:
				msgCase = new ExShowScreenMessage("你已達成 25 連殺！", 4000);
				announceMessage = "已經神一般的存在!!!";
				break;
			case 30:
				msgCase = new ExShowScreenMessage("你已達成 MAX 連殺！", 4000);
				announceMessage = "已經超越神的領域!!!";
				break;
			default:
		}
		
		if ((msgCase != null) && (announceMessage != null))
		{
			player.sendPacket(msgCase);
			Broadcast.toAllOnlinePlayers(new CreatureSay(1, Say2.CRITICAL_ANNOUNCE, "", "PvP 管理員：" + player.getName() + " " + announceMessage));
		}
	}
	
	public static SpreeHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final SpreeHandler _instance = new SpreeHandler();
	}
}