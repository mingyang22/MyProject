package net.yangming.chat;

public class findStringAction {
	public class findInfo {
		public int iFrom;
		public int iTo;
		public String sBetween;
		public findInfo() {
		}
	}
	
	public findInfo findBetween(String strSrc, String strFrom, String strTo) {
		int iFrom = strSrc.indexOf(strFrom);
		if (iFrom >= 0) {
			int iNumStart = iFrom + strFrom.length();
			int iTo = strSrc.indexOf(strTo, iNumStart);
			if (iTo > iNumStart) {
				findInfo obj = new findInfo();
				obj.sBetween = strSrc.substring(iNumStart, iTo);
				obj.iFrom = iFrom;
				obj.iTo = iTo+1;
				return obj;
			}
		}
		return null;
	}
	
	public findInfo compare(findInfo obj1, findInfo obj2) {
		if (obj1 == null) 
			return obj2;
		if (obj2 == null)
			return obj1;
		if (obj1.iFrom < obj2.iFrom)
			return obj1;
		else
			return obj2;
	}
}
