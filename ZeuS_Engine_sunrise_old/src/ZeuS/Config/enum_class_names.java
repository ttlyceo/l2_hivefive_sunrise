package ZeuS.Config;

import java.util.HashMap;
import java.util.Map;

public enum enum_class_names {
	CLASS_FIGHTER(0,"人類戰士","人類","icon.skillhuman"),
	CLASS_WARRIOR(1,"鬥士","人類","icon.skillhuman"),
	CLASS_GLADIATOR(2,"劍鬥士","人類","icon.skillhuman"),
	CLASS_WARLORD(3,"傭兵","人類","icon.skillhuman"),
	CLASS_KNIGHT(4,"騎士","人類","icon.skillhuman"),
	CLASS_PALADIN(5,"聖騎士","人類","icon.skillhuman"),
	CLASS_DARK_AVENGER(6,"闇騎士","人類","icon.skillhuman"),
	CLASS_ROGUE(7,"Rogue","盜賊","icon.skillhuman"),
	CLASS_TREASURE_HUNTER(8,"寶藏獵人 Hunter","人類","icon.skillhuman"),
	CLASS_HAWKEYE(9,"鷹眼","人類","icon.skillhuman"),
	CLASS_MAGE(10,"人類法師","人類","icon.skillhuman"),
	CLASS_WIZARD(11,"巫師","人類","icon.skillhuman"),
	CLASS_SORCERER(12,"術士","人類","icon.skillhuman"),
	CLASS_NECROMANCER(13,"死靈法師","人類","icon.skillhuman"),
	CLASS_WARLOCK(14,"法魔","人類","icon.skillhuman"),
	CLASS_CLERIC(15,"牧師","人類","icon.skillhuman"),
	CLASS_BISHOP(16,"主教","人類","icon.skillhuman"),
	CLASS_PROPHET(17,"先知","人類","icon.skillhuman"),
	CLASS_ELVEN_FIGHTER(18,"精靈戰士","精靈","icon.skillelf"),
	CLASS_ELVEN_KNIGHT(19,"精靈騎士","精靈","icon.skillelf"),
	CLASS_TEMPLE_KNIGHT(20,"聖殿騎士","精靈","icon.skillelf"),
	CLASS_SWORDSINGER(21,"劍術詩人","精靈","icon.skillelf"),
	CLASS_ELVEN_SCOUT(22,"精靈巡守","精靈","icon.skillelf"),
	CLASS_PLAINS_WALKER(23,"大地行者","精靈","icon.skillelf"),
	CLASS_SILVER_RANGER(24,"銀月遊俠","精靈","icon.skillelf"),
	CLASS_ELVEN_MAGE(25,"精靈法師","精靈","icon.skillelf"),
	CLASS_ELVEN_WIZARD(26,"精靈巫師","精靈","icon.skillelf"),
	CLASS_SPELLSINGER(27,"咒術詩人","精靈","icon.skillelf"),
	CLASS_ELEMENTAL_SUMMONER(28,"元素使","精靈","icon.skillelf"),
	CLASS_ORACLE(29,"精靈神使","精靈","icon.skillelf"),
	CLASS_ELDER(30,"精靈長老","黑暗精靈","icon.skillelf"),
	CLASS_DARK_FIGHTER(31,"黑暗精靈戰士","黑暗精靈","icon.skilldarkelf"),
	CLASS_PALUS_KNIGHTR(32,"沼澤騎士","黑暗精靈","icon.skilldarkelf"),
	CLASS_SHILLIEN_KNIGHT(33,"席琳騎士","黑暗精靈","icon.skilldarkelf"),
	CLASS_BLADEDANCER(34,"劍刃舞者","黑暗精靈","icon.skilldarkelf"),
	CLASS_ASSASIN(35,"暗殺者","黑暗精靈","icon.skilldarkelf"),
	CLASS_ABYSS_WALKER(36,"深淵行者","黑暗精靈","icon.skilldarkelf"),
	CLASS_PHANTOM_RANGER(37,"闇影遊俠","黑暗精靈","icon.skilldarkelf"),
	CLASS_DARK_MAGE(38,"黑暗精靈法師","黑暗精靈","icon.skilldarkelf"),
	CLASS_DARK_WIZARD(39,"黑暗巫師","黑暗精靈","icon.skilldarkelf"),
	CLASS_SPELLHOWLER(40,"狂咒術士","黑暗精靈","icon.skilldarkelf"),
	CLASS_PHANTOM_SUMMONER(41,"闇影召喚士","黑暗精靈","icon.skilldarkelf"),
	CLASS_SHILLIEN_ORACLE(42,"席琳神使","黑暗精靈","icon.skilldarkelf"),
	CLASS_SHILIEN_ELDER(43,"席琳長老","黑暗精靈","icon.skilldarkelf"),
	CLASS_ORC_FIGHTER(44,"半獸人戰士","半獸人","icon.skillorc"),
	CLASS_ORC_RAIDER(45,"突襲者","半獸人","icon.skillorc"),
	CLASS_DESTROYER(46,"破壞者","半獸人","icon.skillorc"),
	CLASS_ORC_MONK(47,"武者","半獸人","icon.skillorc"),
	CLASS_TYRANT(48,"暴君","半獸人","icon.skillorc"),
	CLASS_ORC_MAGE(49,"半獸人法師","半獸人","icon.skillorc"),
	CLASS_ORC_SHAMAN(50,"巫醫","半獸人","icon.skillorc"),
	CLASS_OVERLORD(51,"霸主","半獸人","icon.skillorc"),
	CLASS_WARCRYER(52,"戰狂","半獸人","icon.skillorc"),
	CLASS_DWARVEN_FIGHTER(53,"矮人戰士","矮人","icon.skilldwarf"),
	CLASS_SCAVENGER(54,"收集者","矮人","icon.skilldwarf"),
	CLASS_BOUNTY_HUNTER(55,"賞金獵人","矮人","icon.skilldwarf"),
	CLASS_ARTISAN(56,"工匠","矮人","icon.skilldwarf"),
	CLASS_WARSMITH(57,"戰爭工匠","矮人","icon.skilldwarf"),
	CLASS_DUELIST(88,"決鬥者","人類","icon.skillhuman"),
	CLASS_DREADNOUGHT(89,"猛將","人類","icon.skillhuman"),
	CLASS_PHOENIX_KNIGHT(90,"聖凰騎士","人類","icon.skillhuman"),
	CLASS_HELL_KNIGHT(91,"煉獄騎士","人類","icon.skillhuman"),
	CLASS_SAGITTARIUS(92,"人馬","人類","icon.skillhuman"),
	CLASS_ADVENTURER(93,"冒險英豪","人類","icon.skillhuman"),
	CLASS_ARCHMAGE(94,"大魔導士","人類","icon.skillhuman"),
	CLASS_SOULTAKER(95,"魂狩術士","人類","icon.skillhuman"),
	CLASS_ARCANA_LORD(96,"秘儀召主","人類","icon.skillhuman"),
	CLASS_CARDINAL(97,"樞機主教","人類","icon.skillhuman"),
	CLASS_HIEROPHANT(98,"昭聖者","人類","icon.skillhuman"),
	CLASS_EVAS_TEMPLAR(99,"伊娃神殿騎士","精靈","icon.skillelf"),
	CLASS_SWORD_MUSE(100,"伊娃吟遊詩人","精靈","icon.skillelf"),
	CLASS_WIND_RIDER(101,"疾風浪人","精靈","icon.skillelf"),
	CLASS_MOONLIGHT_SENTINEL(102,"月光箭靈","精靈","icon.skillelf"),
	CLASS_MYSTIC_MUSE(103,"伊娃秘術詩人","精靈","icon.skillelf"),
	CLASS_ELEMENTAL_MASTER(104,"元素支配者","精靈","icon.skillelf"),
	CLASS_EVAS_SAINT(105,"伊娃聖者","精靈","icon.skillelf"),
	CLASS_SHILLIEN_TEMPLAR(106,"席琳冥殿騎士","黑暗精靈","icon.skilldarkelf"),
	CLASS_SPECTRAL_DANCER(107,"幽冥舞者","黑暗精靈","icon.skilldarkelf"),
	CLASS_GHOST_HUNTER(108,"魅影獵者","黑暗精靈","icon.skilldarkelf"),
	CLASS_GHOST_SENTINEL(109,"幽冥箭靈","黑暗精靈","icon.skilldarkelf"),
	CLASS_STORM_SCREAMER(110,"暴風狂嘯者","黑暗精靈","icon.skilldarkelf"),
	CLASS_SPECTRAL_MASTER(111,"闇影支配者","黑暗精靈","icon.skilldarkelf"),
	CLASS_SHILLIEN_SAINT(112,"席琳聖者","黑暗精靈","icon.skilldarkelf"),
	CLASS_TITAN(113,"泰坦","半獸人","icon.skillorc"),
	CLASS_GRAND_KHAVATARI(114,"卡巴塔里宗師","半獸人","icon.skillorc"),
	CLASS_DOMINATOR(115,"君主","半獸人","icon.skillorc"),
	CLASS_DOOMCRYER(116,"末日戰狂","半獸人","icon.skillorc"),
	CLASS_FORTUNE_SEEKER(117,"財富獵人","矮人","icon.skilldwarf"),
	CLASS_MAESTRO(118,"巨匠","矮人","icon.skilldwarf"),
	CLASS_MALE_SOLDIER(123,"闇天使士兵〔男〕","闇天使","icon.skillkamael"),
	CLASS_FEMALE_SOLDIER(124,"闇天使士兵〔女〕","闇天使","icon.skillkamael"),
	CLASS_TROOPER(125,"裝甲突襲兵","闇天使","icon.skillkamael"),
	CLASS_WARDER(126,"狙擊衛士","闇天使","icon.skillkamael"),
	CLASS_BERSERKER(127,"狂戰士","闇天使","icon.skillkamael"),
	CLASS_MALE_SOULBREAKER(128,"碎魂者〔男〕","闇天使","icon.skillkamael"),
	CLASS_FEMALE_SOULBREAKER(129,"碎魂者〔女〕","闇天使","icon.skillkamael"),
	CLASS_ARBALESTER(130,"弩弓遊俠","闇天使","icon.skillkamael"),
	CLASS_DOOMBRINGER(131,"末日使者","闇天使","icon.skillkamael"),
	CLASS_MALE_SOULHOUND(132,"追魂使〔男〕","闇天使","icon.skillkamael"),
	CLASS_FEMALE_SOULHOUND(133,"追魂使〔女〕","闇天使","icon.skillkamael"),
	CLASS_TRICKSTER(134,"魔彈射手","闇天使","icon.skillkamael"),
	CLASS_INSPECTOR(135,"戰鬥巡官","闇天使","icon.skillkamael"),
	CLASS_JUDICATOR(136,"軍武判官","闇天使","icon.skillkamael");
	
	enum classData{
		CLASS_NAME,
		CLASS_RACE_NAME,
		CLASS_RACE_ICON
	};

	private int idClass;
	private String ClassName;
	private String RaceName;
	private String RaceIcon;
	
	private Map<Integer, _fastInfoClass> FastClassAccess = new HashMap<Integer, _fastInfoClass>();
	
	private enum_class_names(int _idClass, String _ClassName, String _RaceName, String _RaceIcon){
		idClass = _idClass;
		ClassName = _ClassName;
		RaceName = _RaceName;
		RaceIcon = _RaceIcon;
	}
	
	public String getClassData(int IdClass, classData _type){
		String _Return = "";
		if(FastClassAccess!=null){
			if(FastClassAccess.size()>0){
				if(FastClassAccess.containsKey(IdClass)){
					switch (_type) {
						case CLASS_NAME:
							return FastClassAccess.get(IdClass).getClassName();
						case CLASS_RACE_ICON:
							return FastClassAccess.get(IdClass).getRaceIcon();
						case CLASS_RACE_NAME:
							return FastClassAccess.get(IdClass).getRaceName();
					}
				}
			}
		}
		for(enum_class_names temp : values()){
			if(FastClassAccess==null){
				_fastInfoClass Ctemp = new _fastInfoClass(temp.getClassName(), temp.getRaceName(), temp.getRaceIcon());
				FastClassAccess.put(temp.idClass, Ctemp);
			}else if(FastClassAccess.size()==0){
				_fastInfoClass Ctemp = new _fastInfoClass(temp.getClassName(), temp.getRaceName(), temp.getRaceIcon());
				FastClassAccess.put(temp.idClass, Ctemp);				
			}else if(!FastClassAccess.containsKey(temp.idClass)){
				_fastInfoClass Ctemp = new _fastInfoClass(temp.getClassName(), temp.getRaceName(), temp.getRaceIcon());
				FastClassAccess.put(temp.idClass, Ctemp);				
			}
			if(IdClass == temp.idClass){
				switch (_type) {
				case CLASS_NAME:
					return temp.ClassName;
				case CLASS_RACE_ICON:
					return temp.RaceIcon;
				case CLASS_RACE_NAME:
					return temp.RaceName;

				}
			}
		}
		return _Return;
	}

	public int getIdClass(){
		return idClass;
	}
	public String getClassName(){
		return ClassName;
	}
	public String getRaceName(){
		return RaceName;
	}
	public String getRaceIcon(){
		return RaceIcon;
	}
}

class _fastInfoClass{
	private String ClassName;
	private String RaceName;
	private String RaceIcon;
	public _fastInfoClass(String _ClassName, String _RaceName, String _RaceIcon){
		this.ClassName = _ClassName;
		this.RaceName = _RaceName;
		this.RaceIcon = _RaceIcon;
	}
	public final String getClassName(){
		return this.ClassName;
	}
	public final String getRaceName(){
		return this.RaceName;
	}
	public final String getRaceIcon(){
		return this.RaceIcon;
	}
}
