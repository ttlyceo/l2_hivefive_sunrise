package top.jsoft.jguard.manager;

import l2r.gameserver.LoginServerThread;
import l2r.gameserver.network.L2GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jsoft.jguard.JGuard;
import top.jsoft.jguard.database.ProtectionDAO;
import top.jsoft.jguard.manager.bans.JBanManager;
import top.jsoft.jguard.manager.bans.model.JBan;
import top.jsoft.jguard.manager.hwid.HWIDInfo;
import top.jsoft.jguard.manager.session.model.HWID;

import java.util.Map;

/**
 * Created by psygrammator
 * group jsoft.top
 */
public class HWIDBanManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HWIDBanManager.class);
    private Map<Integer, HWIDInfo> listHWIDBanned;

    private HWIDBanManager() {
        load();

        LOGGER.info("HWIDManager: Loaded " + listHWIDBanned.size() + " HWIDs banned");
    }

    public static HWIDBanManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void load() {
        listHWIDBanned = ProtectionDAO.getInstance().loadHWIDListBanned();
    }

    public BanType checkIsHWIDBanned(final L2GameClient client) {
        if (listHWIDBanned.isEmpty())
            return BanType.NONE;

        for (int i = 0; i < listHWIDBanned.size(); i++)
            if (listHWIDBanned.get(i).getHWID().equals(client.getHWID()))
                return listHWIDBanned.get(i).getBanType();

        return BanType.NONE;
    }

    public BanType checkIsHWIDBannedByName(final L2GameClient client) {
        if (listHWIDBanned.isEmpty())
            return BanType.NONE;

        for (int i = 0; i < listHWIDBanned.size(); i++) {
            if (listHWIDBanned.get(i).getHWID().equals(client.getHWID()) && listHWIDBanned.get(i).getName().equals(client.getActiveChar().getName()))
                return listHWIDBanned.get(i).getBanType();
        }
        return BanType.NONE;
    }

    public void systemBan(final L2GameClient client, final String comment, final BanType banType) {
        final int id = listHWIDBanned.size();
        final HWIDInfo hwidListBan = new HWIDInfo(id);
        if (client.getHWID() == null || client.getHWID().isEmpty()) {
            client.setHWID(JGuard.DEFAULT_HWID);
            LOGGER.warn("Client: " + client.getIPAddress() + " returned empty HWID! Check him!");
        }
        hwidListBan.setHWIDBanned(client.getHWID());
        hwidListBan.setBanType(banType);
        String name = "N/A";
        if(banType == BanType.PLAYER_BAN)
        {
            if(client.getActiveChar() != null)
                name = client.getActiveChar().getName();
        }
        else if(banType == BanType.ACCOUNT_BAN)
            name = client.getLogin();
        hwidListBan.setName(name);
        switch (banType) {
            case NONE:
                client.getActiveChar().logout();
                break;
            case PLAYER_BAN:
                ProtectionDAO.getInstance().storeHWIDBanned(client, comment, banType);
                JBanManager.addBan(new JBan(HWID.fromString(client.getHWID()), 0, comment, "ADMIN"));
                //AutoBan.banPlayer(client.getActiveChar(), 365, comment, "SYSTEM_BOT");
                client.getActiveChar().logout();
                break;
            case ACCOUNT_BAN:
                ProtectionDAO.getInstance().storeHWIDBanned(client, comment, banType);
                JBanManager.addBan(new JBan(HWID.fromString(client.getHWID()), 0, comment, "ADMIN"));
                LoginServerThread.getInstance().sendAccessLevel( client.getLogin(), -100);
                client.getActiveChar().logout();
                break;
        }
        listHWIDBanned.put(id, hwidListBan);
    }

    public enum BanType {
        NONE,
        PLAYER_BAN,
        ACCOUNT_BAN,
    }

    private static class LazyHolder {
        private static final HWIDBanManager INSTANCE = new HWIDBanManager();
    }
}
