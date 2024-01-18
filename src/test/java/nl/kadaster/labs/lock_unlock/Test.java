package nl.kadaster.labs.lock_unlock;

import tdb2.tdbstats;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] graphs= {"https://data.federatief.datastelsel.nl/lock-unlock/gemeentenamen","https://data.federatief.datastelsel.nl/lock-unlock/anbi","https://data.labs.kadaster.nl/lock-unlock/koopsom","https://data.federatief.datastelsel.nl/lock-unlock/nhr","https://data.federatief.datastelsel.nl/lock-unlock/brp","https://data.labs.kadaster.nl/lock-unlock/brk","https://data.labs.kadaster.nl/Unlock/unlock/rkkgpercelen"};
		
		for (String graph:graphs)
		{
		
			String[] s= {"--loc=C:/DSchijf/dbdata/fusekidb/databases/unlocked","--graph="+graph};
			tdbstats.main(s );
		}
	}

}
