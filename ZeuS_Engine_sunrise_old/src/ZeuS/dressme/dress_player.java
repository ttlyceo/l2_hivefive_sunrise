package ZeuS.dressme;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.InventoryUpdate;
import l2r.gameserver.model.itemcontainer.Inventory;
import ZeuS.Comunidad.EngineForm.v_Dressme;
import ZeuS.Config._dress;
import ZeuS.Config._dress.BodyPart;
import ZeuS.Config._dressItem;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;

public class dress_player {
	//CHEST=42188;LEGS=42190;GLOVES=42191;FEET=42192;RHAND=14120;LHAND=0;CLOAK=0
	
	private static final Logger _log = Logger.getLogger(dress_player.class.getName());
	
	private final String CadenaDressme = "CHEST=%CH%;LEGS=%LGS%;GLOVES=%GLO%;FEET=%FEET%;RHAND=%RH%;LHAND=%LH%;CLOAK=%CLO%;HOOD1=%HOO1%;HOOD2=%HOO2%";
	private final String SQL_INSERT = "call sp_dressme(?,?,?,?)";	
	private Map<Integer, _dress> DRESSME_PLAYER = new HashMap<Integer, _dress>();
	private int ID_DRESSME_USE = 0;
	private boolean CAN_SEE_OTHERS_DRESSMES = true;
	private Integer ID_PLAYER;
	private final String SaveItem = "INSERT INTO zeus_dressme_items_char() values (?,?)";
	
	public int getIdDressmeUsing(){
		return ID_DRESSME_USE;
	}
	
	public void setCleanDressme(int idDressme){
		_dress drs = DRESSME_PLAYER.get(idDressme);
		drs.cleanAllDressme();
		update_DressmeItem(idDressme);
		if(idDressme == ID_DRESSME_USE){
			updateCharacter();
		}		
	}
	
	public boolean setTry(int Slot, int IdItem){
		_dress drs = DRESSME_PLAYER.get(this.ID_DRESSME_USE);
		return drs.setTry(Slot, IdItem);
	}
	
	public void setIdDressmeToPart(int Slot, int idItem, int idDressme) {
		_dressItem _tempData = null;
		_dress drs = DRESSME_PLAYER.get(idDressme);

		// 先設定該部位的時裝道具 ID
		drs.SetDress(Slot, idItem);

		// 特殊處理頭飾 Slot，分開處理 HEAD_1 / HEAD_2
		if (Slot == Inventory.PAPERDOLL_HAIR) {
			drs.setHood1(idItem); // 僅設定 HEAD_1
		} else if (Slot == Inventory.PAPERDOLL_HAIR2) {
			drs.setHood2(idItem); // 僅設定 HEAD_2
		}

		update_DressmeItem(idDressme);

		if (idDressme == ID_DRESSME_USE) {
			updateCharacter();
		}

		// 判斷是否需要儲存到資料庫（頭飾不執行特殊專屬排除）
		if (idItem > 0) {
			_tempData = dressme.DRESSME_ITEM_CONFIG.get(idItem).getDressItem();
			_tempData.loadExclusiveData();

			if (_tempData._isClan_exclusive() && _tempData.haveOwnerClan()) {
				return; // 不儲存
			}
			saveItemPartInDB(idItem);
		}
	}


	
	private void saveItemPartInDB(int _IdItem) {
		Connection con = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(SaveItem);
			ins.setInt(1, this.ID_PLAYER);
			ins.setInt(2, _IdItem);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			v_Dressme.setNewItemToPlayer(this.ID_PLAYER, _IdItem);
		}catch(Exception a){
			
		}	
		try {
			con.close();
		}catch (Exception e) {
			
		}
	}
	
	public void useDressme(int idDressToUse){
		L2PcInstance PLAYER = opera.getPlayerbyID(ID_PLAYER);
		
		if(idDressToUse==0){
			ID_DRESSME_USE = 0;
			update_UsingDressme();
			updateCharacter();
			return;
		}
		
		_dress drs = DRESSME_PLAYER.get(idDressToUse);
		if(!drs.isEmpty()){
			drs.setNewDressmeUse(idDressToUse);
			ID_DRESSME_USE = idDressToUse;
			updateCharacter();
			update_UsingDressme();
		}else{
			central.msgbox(language.getInstance().getMsg(PLAYER).DRESSME_YOU_CANT_USE_THIS_DRESSME_IS_EMPTY.replace("$dressme", String.valueOf(idDressToUse)), PLAYER);
		}
	}
	
	public final boolean canSeeOthersDressme() {
		return this.CAN_SEE_OTHERS_DRESSMES;
	}
	
	public void changeDressmeShow(boolean _status) {
		this.CAN_SEE_OTHERS_DRESSMES = _status;
		updateCharacter();
		update_UsingDressme();		
	}
	
	private void update_UsingDressme(){
		String mysqlUpdate = "update zeus_dressme set used=?, seeOtherDressme=? where idChar=?";
		Connection con = null;
		try{
			L2PcInstance PLAYER = opera.getPlayerbyID(ID_PLAYER);
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(mysqlUpdate);
			ins.setInt(1, ID_DRESSME_USE);
			ins.setString(2, ( CAN_SEE_OTHERS_DRESSMES ? "yes" : "no" ));
			ins.setInt(3, PLAYER.getObjectId());
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				_log.warning("Dressme Error Update -> " + e.getMessage());
			}
			
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch (Exception e) {
			
		}		
	}
	
	private void update_DressmeItem(int idDressme){
		//"CHEST=%CH%;LEGS=%LGS%;GLOVES=%GLO%;FEET=%FEET%;RHAND=%RH%;LHAND=%LH%;CLOAK=%CLO%";
		_dress drs = DRESSME_PLAYER.get(idDressme);		
		String SendSql = CadenaDressme.replace("%CH%", String.valueOf(drs.getIds(BodyPart.CHEST)));
		SendSql = SendSql.replace("%LGS%", String.valueOf(drs.getIds(BodyPart.LEGS)));
		SendSql = SendSql.replace("%GLO%", String.valueOf(drs.getIds(BodyPart.GLOVES)));
		SendSql = SendSql.replace("%FEET%", String.valueOf(drs.getIds(BodyPart.FEET)));
		SendSql = SendSql.replace("%RH%", String.valueOf(drs.getIds(BodyPart.R_HAND)));
		SendSql = SendSql.replace("%LH%", String.valueOf(drs.getIds(BodyPart.L_HAND)));
		SendSql = SendSql.replace("%CLO%", String.valueOf(drs.getIds(BodyPart.CLOAK)));
		SendSql = SendSql.replace("%HOO1%", String.valueOf(drs.getIds(BodyPart.HEAD_1)));
		SendSql = SendSql.replace("%HOO2%", String.valueOf(drs.getIds(BodyPart.HEAD_2)));
		String mysqlUpdate = "update zeus_dressme set d"+ String.valueOf(idDressme) +"=? where idChar=?";
		Connection con = null;
		try{
			L2PcInstance PLAYER = opera.getPlayerbyID(ID_PLAYER);
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(mysqlUpdate);
			ins.setString(1, SendSql);
			ins.setInt(2, PLAYER.getObjectId());
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch (Exception e) {
			
		}		
	}	
	
	private void updateCharacter(){
		L2PcInstance PLAYER = opera.getPlayerbyID(ID_PLAYER);
		InventoryUpdate iu = new InventoryUpdate();
		PLAYER.sendPacket(iu);
		PLAYER.broadcastUserInfo();
		InventoryUpdate iu2 = new InventoryUpdate();
		PLAYER.sendPacket(iu2);
		PLAYER.broadcastUserInfo();
	}	

	public void updateIsTheSame(int idDressme){
		if(idDressme == ID_DRESSME_USE){
			updateCharacter();
		}
	}
	
	public _dress getDressme(){
		try{
			return DRESSME_PLAYER.get(ID_DRESSME_USE);
		}catch(Exception a){
			
		}
		return null;
	}
	
	public _dress getDressme(int idDress){
		try{
			return DRESSME_PLAYER.get(idDress);
		}catch(Exception a){
			
		}
		return null;
	}
	
	public dress_player(int IdPlayer){
		getDressmePlayer(IdPlayer);
	}
	
	public int getIds(int idSlot, boolean isFromCharInfo, boolean isFullDress){
		if(ID_DRESSME_USE == 0 && !DRESSME_PLAYER.get(ID_DRESSME_USE).isTrySet() ){
			return -100;
		}
		BodyPart partIndex;
switch (idSlot) {
	case Inventory.PAPERDOLL_HAIR:
		partIndex = BodyPart.HEAD_1;
		break;
	case Inventory.PAPERDOLL_HAIR2:
		partIndex = BodyPart.HEAD_2;
		break;
	case Inventory.PAPERDOLL_RHAND:
		partIndex = BodyPart.R_HAND;
		break;
	case Inventory.PAPERDOLL_LHAND:
		partIndex = BodyPart.L_HAND;
		break;
	case Inventory.PAPERDOLL_CHEST:
		partIndex = BodyPart.CHEST;
		break;
	case Inventory.PAPERDOLL_LEGS:
		partIndex = BodyPart.LEGS;
		break;
	case Inventory.PAPERDOLL_GLOVES:
		partIndex = BodyPart.GLOVES;
		break;
	case Inventory.PAPERDOLL_FEET:
		partIndex = BodyPart.FEET;
		break;
	case Inventory.PAPERDOLL_CLOAK:
		partIndex = BodyPart.CLOAK;
		break;
	default:
		return 0; // 若無對應就不處理
}int idBack = DRESSME_PLAYER.get(ID_DRESSME_USE).getIds(partIndex);
		// int idBack = DRESSME_PLAYER.get(ID_DRESSME_USE).getIds(idSlot, isFromCharInfo, isFullDress);
		boolean isTry = DRESSME_PLAYER.get(ID_DRESSME_USE).isTrySet();
		if(idBack > 0){
			if(dressme.DRESSME_ITEM_CONFIG.get(idBack).isOnlyHero()){
				L2PcInstance ppl = opera.getPlayerbyID(ID_PLAYER);
				if(!ppl.isHero() && !ppl.isGM() && !isTry){
					central.msgbox( language.getInstance().getMsg(ppl).DRESSME_JUST_FOR_HERO , ppl);
					idBack = -100;
				}
			}else if(dressme.DRESSME_ITEM_CONFIG.get(idBack).isOnlyNoble()){
				L2PcInstance ppl = opera.getPlayerbyID(ID_PLAYER);
				if(!ppl.isNoble() && !ppl.isGM() && !isTry){
					central.msgbox( language.getInstance().getMsg(ppl).DRESSME_JUST_FOR_NOBLE , ppl);
					idBack = -100;
				}
			}else if(dressme.DRESSME_ITEM_CONFIG.get(idBack).isOnlyTopPvPPK()){
				L2PcInstance ppl = opera.getPlayerbyID(ID_PLAYER);
				if(!opera.isTopPvPPk(ppl) && !ppl.isGM() && !isTry){
					central.msgbox( language.getInstance().getMsg(ppl).DRESSME_JUST_FOR_TOP_PVP_PK , ppl);
					idBack = -100;
				}					
			}else if(dressme.DRESSME_ITEM_CONFIG.get(idBack).isOnlyForClan() || dressme.DRESSME_ITEM_CONFIG.get(idBack).isOnlyForChar()){
				_dressItem dI = dressme.DRESSME_ITEM_CONFIG.get(idBack).getDressItem();
				if(dI == null){
					idBack = -100;
				}
				L2PcInstance ppl = opera.getPlayerbyID(ID_PLAYER);
				if(!isTry && !ppl.isGM()){
					if(dI.haveOwnerClan()){
						if(ppl.getClan()==null){
							central.msgbox(language.getInstance().getMsg(ppl).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, ppl);
							idBack = -100;
						}else if(!dI._isOwnerClan(ppl.getClanId())){
							central.msgbox(language.getInstance().getMsg(ppl).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, ppl);
							idBack = -100;
						}						
					}else if(dI.haveOwnerChar()){
						if(!dI._isOwnerChar(ppl.getObjectId())){
							central.msgbox(language.getInstance().getMsg(ppl).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, ppl);
							idBack = -100;
						}						
					}
				}
			}
		}
		return idBack;
	}
	
	public int getIds(int idSlot, int IdDressme, boolean isFromCharInfo, boolean isFullDress){
		if(IdDressme == 0){
			return 0;
		}
		return DRESSME_PLAYER.get(IdDressme).getIds(idSlot,isFromCharInfo, isFullDress);
	}	
	
	protected void getDressmePlayer(int idplayer){
		if(DRESSME_PLAYER!=null){
			if(!DRESSME_PLAYER.containsKey(0)){
				_dress Vestimenta = new _dress(0,0,0,0,0,0,0,0,0,0);
				DRESSME_PLAYER.put(0, Vestimenta);
			}
		}else{
			_dress Vestimenta = new _dress(0,0,0,0,0,0,0,0,0,0);
			DRESSME_PLAYER.put(0, Vestimenta);			
		}		
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			CallableStatement psqry = conn.prepareCall(SQL_INSERT);
			psqry.setInt(1,2);
			psqry.setInt(2, idplayer);
			psqry.setInt(3,-1);
			psqry.setString(4, "");
			ResultSet rss = psqry.executeQuery();
			if(rss.next()){
				if(!rss.getString(1).equals("err")){
					this.ID_DRESSME_USE = rss.getInt("used");
					this.CAN_SEE_OTHERS_DRESSMES = ( rss.getString("seeOtherDressme").equalsIgnoreCase("yes") ? true : false );
					ID_PLAYER = idplayer;
					for(int i=1;i<=10;i++){
						if(rss.getString(i).trim().length()>0){
							String Cadena[] = rss.getString(i).split(";");
							int idchest = Integer.valueOf(Cadena[0].split("=")[1]);
							int idlegs = Integer.valueOf(Cadena[1].split("=")[1]);
							int idgloves = Integer.valueOf(Cadena[2].split("=")[1]);
							int idfeet = Integer.valueOf(Cadena[3].split("=")[1]);
							int idrhand = Integer.valueOf(Cadena[4].split("=")[1]);
							int idlhand = Integer.valueOf(Cadena[5].split("=")[1]);
							int idcloak = Integer.valueOf(Cadena[6].split("=")[1]);
							int idHood1 = 0;
							int idHood2 = 0;
							try {
								idHood1 = Integer.valueOf(Cadena[7].split("=")[1]);
							}catch(Exception a) {
								idHood1 = 0;
							}
							try {
								idHood2 = Integer.valueOf(Cadena[8].split("=")[1]);
							}catch(Exception a) {
								idHood2 = 0;
							}
							_dress Vestimenta = new _dress(idcloak, idrhand, idlhand, idchest, idlegs, idgloves, idfeet, ID_DRESSME_USE, idHood1, idHood2);
							DRESSME_PLAYER.put(i, Vestimenta);
						}else{
							_dress Vestimenta = new _dress(0,0,0,0,0,0,0,0,0,0);
							DRESSME_PLAYER.put(i, Vestimenta);							
						}
					}
				}else {
					for(int i=1;i<=10;i++){
						_dress Vestimenta = new _dress(0,0,0,0,0,0,0,0,0,0);
						DRESSME_PLAYER.put(i, Vestimenta);
					}
				}
			}
		}catch(Exception a){
			_log.warning("Error loading Dressme from player-> Id Player" + idplayer + " / " + a.getMessage());
		}
		try {
			conn.close();
		}catch (Exception e) {
			
		}		
    }
}