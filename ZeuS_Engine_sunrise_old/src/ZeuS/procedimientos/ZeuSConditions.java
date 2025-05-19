package ZeuS.procedimientos;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import ZeuS.Config._cumulativeData;
import l2r.Config;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.base.ClassId;
import l2r.gameserver.model.base.PlayerClass;
import l2r.gameserver.model.base.SubClass;
import l2r.gameserver.model.entity.olympiad.OlympiadManager;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;


/**
 * @author Jabberwock
 * @Website www.l2zeus.net
 */
public class ZeuSConditions
{
	private ZeuSConditions()
	{
		// Dummy default
	}
	
	/**
	 * Check the general requirements for the player to be able to use it
	 * @param player [L2PcInstance]
	 * @return boolean
	 */
	public static boolean checkPlayerConditions(L2PcInstance player)
	{
		if (player.isGM())
		{
			return true;
		}
		
		// Restrictions added here
		if (player.isInOlympiadMode() || player.isInOlympiad() || player.inObserverMode() || OlympiadManager.getInstance().isRegistered(player))
		{
			player.sendMessage("無法在奧林匹亞競技中使用。");
			return false;
		}
		
		if (player.getKarma() > 0)
		{
			player.sendMessage("無法在擁有性向值時使用。");
			return false;
		}
		
		if (player.isJailed())
		{
			player.sendMessage("在監獄中無法使用。");
			return false;
		}
		
		if (player.isEnchanting())
		{
			player.sendMessage("無法在進行強化時使用。");
			return false;
		}
		
		if (player.isAlikeDead())
		{
			player.sendMessage("無法在死亡或假死狀態下使用。");
			return false;
		}
		
		if (player.isInsideZone(ZoneIdType.ZONE_CHAOTIC))
		{
			player.sendMessage("無法在混亂區域使用。");
			return false;
		}
		
		if ((player.getPvpFlag() != 0) && !player.isInsideZone(ZoneIdType.PEACE))
		{
			player.sendMessage("在PVP標記中無法使用。");
			return false;
		}
		
		if (player.isInCombat() && !player.isInsideZone(ZoneIdType.PEACE))
		{
			player.sendMessage("在戰鬥中無法使用。");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method to check if it is a valid clan name
	 * @param name
	 * @return
	 */
	public static boolean isValidName(String name)
	{
		Pattern pattern;
		try
		{
			pattern = Pattern.compile(Config.CLAN_NAME_TEMPLATE);
		}
		catch (PatternSyntaxException e)
		{
			pattern = Pattern.compile(".*");
		}
		return pattern.matcher(name).matches();
	}
	
	public static boolean _checkQuests(L2PcInstance player)
	{
		// Noble players can add Sub-Classes without quests
		if (player.isNoble())
		{
			return true;
		}
		
		QuestState qs = player.getQuestState(Quest.FATES_WHISPER);
		if ((qs == null) || !qs.isCompleted())
		{
			return false;
		}
		
		qs = player.getQuestState(Quest.MIMIRS_ELIXIR);
		if ((qs == null) || !qs.isCompleted())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns list of available subclasses Base class and already used subclasses removed
	 * @param player
	 * @return
	 */
	public final static Set<PlayerClass> getAvailableSubClasses(L2PcInstance player)
	{
		// get player base class
		final int currentBaseId = player.getBaseClass();
		final ClassId baseCID = ClassId.getClassId(currentBaseId);
		
		// we need 2nd occupation ID
		final int baseClassId;
		if (baseCID.level() > 2)
		{
			baseClassId = baseCID.getParent().ordinal();
		}
		else
		{
			baseClassId = currentBaseId;
		}
		
		Set<PlayerClass> availSubs = PlayerClass.values()[baseClassId].getAvailableSubclasses(player);
		Set<PlayerClass> ReturnSub = null;
		Iterator<PlayerClass> availSub = null;
		if ((availSubs != null) && !availSubs.isEmpty())
		{
			for (availSub = availSubs.iterator(); availSub.hasNext();)
			{
				PlayerClass pclass = availSub.next();
				// check for the village master
				if (!checkVillageMaster(pclass))
				{
					availSub.remove();
					continue;
				}
				// scan for already used subclasses
				int availClassId = pclass.ordinal();
				ClassId cid = ClassId.getClassId(availClassId);
				SubClass prevSubClass;
				ClassId subClassId;
				for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
				{
					prevSubClass = subList.next();
					subClassId = ClassId.getClassId(prevSubClass.getClassId());
					
					if (subClassId.equalsOrChildOf(cid))
					{
						availSub.remove();
						break;
					}
				}
			}
			
		}
		
		return availSubs;
	}
	
	/**
	 * Check new subclass classId for validity (villagemaster race/type, is not contains in previous subclasses, is contains in allowed subclasses) Base class not added into allowed subclasses.
	 * @param player
	 * @param classId
	 * @return
	 */
	public final static boolean isValidNewSubClass(L2PcInstance player, int classId)
	{
		if (!checkVillageMaster(classId))
		{
			return false;
		}
		
		final ClassId cid = ClassId.values()[classId];
		SubClass sub;
		ClassId subClassId;
		for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
		{
			sub = subList.next();
			subClassId = ClassId.values()[sub.getClassId()];
			
			if (subClassId.equalsOrChildOf(cid))
			{
				return false;
			}
		}
		
		// get player base class
		final int currentBaseId = player.getBaseClass();
		final ClassId baseCID = ClassId.getClassId(currentBaseId);
		
		// we need 2nd occupation ID
		final int baseClassId;
		if (baseCID.level() > 2)
		{
			baseClassId = baseCID.getParent().ordinal();
		}
		else
		{
			baseClassId = currentBaseId;
		}
		
		Set<PlayerClass> availSubs = PlayerClass.values()[baseClassId].getAvailableSubclasses(player);
		if ((availSubs == null) || availSubs.isEmpty())
		{
			return false;
		}
		
		boolean found = false;
		for (PlayerClass pclass : availSubs)
		{
			if (pclass.ordinal() == classId)
			{
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * Returns true if this classId allowed for master
	 * @param classId
	 * @return
	 */
	public final static boolean checkVillageMaster(int classId)
	{
		return checkVillageMaster(PlayerClass.values()[classId]);
	}
	
	/**
	 * Returns true if this PlayerClass is allowed for master
	 * @param pclass
	 * @return
	 */
	public final static boolean checkVillageMaster(PlayerClass pclass)
	{
		return true;
	}
	
	public static final Iterator<SubClass> iterSubClasses(L2PcInstance player)
	{
		return player.getSubClasses().values().iterator();
	}
	
	public static int getElementalToId(String El)
	{
		int IDElement =0;
		switch(El){
			case "Fire":
				IDElement = 0;
				break;
			case "Water":
				IDElement = 1;
				break;
			case "Wind":
				IDElement = 2;
				break;
			case "Earth":
				IDElement = 3;
				break;
			case "Holy":
				IDElement = 4;
				break;
			case "Dark":
				IDElement = 5;
				break;				
		}
		return IDElement;
 	}	
	
	public static int getIdStoneToId(int idStone)
	{
		switch(idStone){
			case 9558:
				return 0;
			case 9559:
				return 1;
			case 9561:
				return 2;
			case 9560://"Earth":
				return 3;
			case 9562://"Dark":
				return 5;
			case 9563://"Holy":
				return 4;
		}
		return -1;
	}
	
	public static int getStoneIdToID(int idStone)
	{
		switch(idStone){
			case 0:
				return 9558;
			case 1:
				return 9559;
			case 2:
				return 9561;
			case 3://"Earth":
				return 9560;
			case 5://"Dark":
				return 9562;
			case 4://"Holy":
				return 9563;
		}
		return -1;
	}	
	
}