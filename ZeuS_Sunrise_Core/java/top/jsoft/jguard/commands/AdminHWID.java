package top.jsoft.jguard.commands;

import l2r.gameserver.handler.IAdminCommandHandler;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import top.jsoft.jguard.JGuard;
import top.jsoft.jguard.manager.HWIDBanManager;
import top.jsoft.jguard.manager.HWIDListManager;
import top.jsoft.jguard.manager.bans.JBanManager;

import java.util.StringTokenizer;

/**
 * Created by psygrammator
 * group jsoft.top
 */
public class AdminHWID implements IAdminCommandHandler {
    private static final String[] ADMIN_COMMANDS =
            {
                    "admin_hwid_info", "admin_hwid_ban_char", "admin_hwid_ban_acc", "admin_hwid_reload"
            };
    @Override
    public boolean useAdminCommand(final String fullString, final L2PcInstance activeChar) {
        if (!JGuard.isProtectEnabled()) {
            return false;
        }

        final StringTokenizer st = new StringTokenizer(fullString);
        if(fullString.startsWith("admin_hwid_info"))
        {
            if (activeChar.getTarget() != null) {
                final L2Object playerTarger = activeChar.getTarget();
                if (!(playerTarger instanceof L2PcInstance)) {
                    activeChar.sendMessage("Target is not player!");
                    return false;
                }
                final L2PcInstance target = (L2PcInstance) playerTarger;
                if (target != null)
                    activeChar.sendMessage("L2PcInstance name: " + target.getName() + " HWID: " + target.getClient().getHWID());
            } else {
                st.nextToken();
                final String player = st.nextToken();
                final L2PcInstance targetPlayer = L2World.getInstance().getPlayer(player);
                if (targetPlayer != null)
                    activeChar.sendMessage("L2PcInstance name: " + targetPlayer.getName() + " HWID: " + targetPlayer.getClient().getHWID());
                else {
                    activeChar.sendMessage(player + " char name not found.");
                    return false;
                }
            }
        }
        else if(fullString.startsWith("admin_hwid_ban_char"))
        {
            banThisPlayer(activeChar, st, false);
        }
        else if(fullString.startsWith("admin_hwid_ban_acc"))
        {
            banThisPlayer(activeChar, st, true);
        }
        else if(fullString.startsWith("admin_hwid_reload"))
        {
            HWIDBanManager.getInstance().load();
            HWIDListManager.getInstance().load();
            JBanManager.reload();
        }
        return true;
    }

    @SuppressWarnings("unused")
    private boolean banThisPlayer(final L2PcInstance activeChar, final StringTokenizer st, final boolean account) {
        if (activeChar.getTarget() != null) {
            final L2Object playerTarger = activeChar.getTarget();
            if (!playerTarger.isPlayer()) {
                activeChar.sendMessage("Target is not player!");
                return false;
            }
            final L2PcInstance target = (L2PcInstance) playerTarger;
            if (target != null) {
                //ProtectionDAO.getInstance().storeHWIDBanned(target.getClient(), "PERMANENT_HWID_BAN_FROM_GM_" + activeChar.getName(null), account == true ? HWIDBanManager.BanType.ACCOUNT_BAN : HWIDBanManager.BanType.PLAYER_BAN);
                final String playerAcc = account ? " account" : " character";
                final String name = account ? target.getAccountName() : target.getName();
                HWIDBanManager.getInstance().systemBan(target.getClient(), "Admin ban [" + name + playerAcc + "] from: " + activeChar.getName(), account ? HWIDBanManager.BanType.ACCOUNT_BAN : HWIDBanManager.BanType.PLAYER_BAN);
                activeChar.sendMessage(name + playerAcc + " banned on HWID");
                if (account)
                    target.setAccessLevel(-100);
                target.logout();
                return true;
            }
        } else {
            st.nextToken();
            final String player = st.nextToken();
            final L2PcInstance targetPlayer = L2World.getInstance().getPlayer(player);
            if (targetPlayer != null) {
                //ProtectionDAO.getInstance().storeHWIDBanned(targetPlayer.getClient(), "PERMANENT_HWID_BAN_FROM_GM_" + activeChar.getName(null), account == true ? HWIDBanManager.BanType.ACCOUNT_BAN : HWIDBanManager.BanType.PLAYER_BAN);
                final String playerAcc = account ? " account" : " character";
                final String name = account ? targetPlayer.getAccountName() : targetPlayer.getName();
                HWIDBanManager.getInstance().systemBan(targetPlayer.getClient(), "Admin ban [" + name + playerAcc + "] from: " + activeChar.getName(), account ? HWIDBanManager.BanType.ACCOUNT_BAN : HWIDBanManager.BanType.PLAYER_BAN);
                activeChar.sendMessage(name + playerAcc + " banned on HWID");
                if (account)
                    targetPlayer.setAccessLevel(-100);
                targetPlayer.logout();
                return true;
            } else {
                activeChar.sendMessage(player + " char name not found.");
                return false;
            }
        }
        return false;
    }

    @Override
    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }

}
