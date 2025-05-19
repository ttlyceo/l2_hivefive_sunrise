package top.jsoft.jguard.manager.session.model;

import l2r.gameserver.network.L2GameClient;
import l2r.gameserver.network.serverpackets.ServerClose;
import top.jsoft.jguard.JGuardConfig;

import java.util.*;


/**
 * @author Akumu
 * @date 25.10.13
 */
public class JClientSession
{
	private final Set<L2GameClient> _session;
	private final Set<String> _accounts;
	public final HWID hwid;
	public final int langId;

	public JClientSession(JClientData clientData)
	{
		_accounts = new HashSet<String>();
		_session = new HashSet<L2GameClient>();
		hwid = clientData.hwid;
		langId = clientData.langId;
	}

	public List<L2GameClient> getClients()
	{
		clean();

		final List<L2GameClient> res = new ArrayList<L2GameClient>(_session.size());
		res.addAll(_session);
		return res;
	}

	public void addClient(JClientData cd, L2GameClient client)
	{
		_session.add(client);
		_accounts.add(cd.account.toLowerCase());
	}

	public boolean hasAccountSession(String acc)
	{
		if(acc == null)
			return false;

		for (String account : _accounts)
			if (account.equalsIgnoreCase(acc))
				return true;

		return false;
	}

	public boolean canLogin()
	{
		if(JGuardConfig.JGUARD_LIMIT_LOGIN <= 0)
			return true;

		clean();

		return _session.size() < JGuardConfig.JGUARD_LIMIT_LOGIN;
	}

	public int getCount()
	{
		clean();

		return _session.size();
	}

	private void clean()
	{
		Iterator<L2GameClient> it = _session.iterator();
		while(it.hasNext())
		{
			L2GameClient client = it.next();

			//TODO ncs.SpawN переделать L2GameClient.GameClientState.DISCONNECTED
			//if(client == null || !client.isConnected() || client.getState() == L2GameClient.GameClientState.DISCONNECTED || client.getActiveChar() == null)
			//if(client == null || !client.isConnected() || client.getActiveChar() == null)
			//if(client == null || !client.isConnected() || client.getAccountName() == null || client.getAccountName().equalsIgnoreCase(""))
			if(client == null || !client.isConnected() || (client.getState() != L2GameClient.GameClientState.IN_GAME && /*client.getState() != L2GameClient.GameClientState.ENTERING &&*/ client.getState() != L2GameClient.GameClientState.AUTHED) || client.getLogin() == null || client.getLogin().equalsIgnoreCase(""))
			{
				it.remove();
			}
		}
	}
	
	/*
	private void cleanAccounts()
	{
		Iterator<String> it = _accounts.iterator();
		while(it.hasNext())
		{
			String client = it.next();

			//TODO ncs.SpawN переделать L2GameClient.GameClientState.DISCONNECTED
			//if(client == null || !client.isConnected() || client.getState() == L2GameClient.GameClientState.DISCONNECTED || client.getActiveChar() == null)
			if(client == null || !client.isConnected() || client.getActiveChar() == null)
				it.remove();
		}
	}*/

	public void disconnect()
	{
		clean();

		for(L2GameClient client : _session)
		{
			try
			{
				//client.close(ServerClose.STATIC_PACKET);
				client.close(ServerClose.STATIC_PACKET);
			}
			catch (Exception e){}
		}
	}

	public String hwid()
	{
		return hwid.plain;
	}
	
	/*public String account()
	{
		return hwid.plain;
	}*/
	
	public List<String> getAccounts()
	{
		clean();

		final List<String> res = new ArrayList<String>(_accounts.size());
		res.addAll(_accounts);
		return res;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JClientSession that = (JClientSession) o;

		if (langId != that.langId) return false;
		if (!hwid.equals(that.hwid)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return hwid.hashCode();
	}

	@Override
	public String toString() {
		return "ClientSession{" +
				"hwid=" + hwid +
				", langId=" + langId +
				'}';
	}
}
