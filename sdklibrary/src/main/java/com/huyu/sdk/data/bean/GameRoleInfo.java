package com.huyu.sdk.data.bean;


import com.huyu.sdk.data.HY_StateType;

import java.util.Map;


/*******************************************
 * @CLASS:HY_GameRoleInfo
 * @AUTHOR:smile
 *******************************************/
public class GameRoleInfo {
	public static int typeId; // typeId 当前情景，目前支持 enterServer，levelUp，createRole
	public static String roleId;// roleId 当前登录的玩家角色ID，必须为数字，若无，传入userid
	public static String roleName;// roleName
							// 当前登录的玩家角色名，不能为空，不能为null，若无，传入"游戏名称+username"，如"刀塔传奇风吹来的鱼"
	public static String roleLevel;// roleLevel 当前登录的玩家角色等级，必须为数字，若无，传入1
	public static String zoneId;// zoneId 当前登录的游戏区服ID，必须为数字，若无，传入1
	public static String zoneName;// zoneName
							// 当前登录的游戏区服名称，不能为空，不能为null，若无，传入游戏名称+"1区"，如"刀塔传奇1区"
	public static int balance;// balance 当前用户游戏币余额，必须为数字，若无，传入0
	public static String vip;// vip 当前用户VIP等级，必须为数字，若无，传入1
	public static String partyName;// partyName 当前用户所属帮派，不能为空，不能为null，若无，传入"无帮派"
	public static String createTime; // 角色创建时间

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	@Override
	public String toString() {
		switch (typeId) {
		case HY_StateType.LEVEL_UP:

			return "HY_GameRoleInfo [上传类型=" + "升级" + ", roleId=" + roleId
					+ ", roleName=" + roleName + ", roleLevel=" + roleLevel
					+ ", zoneId=" + zoneId + ", zoneName=" + zoneName
					+ ", balance=" + balance + ", vip=" + vip + ", partyName="
					+ partyName + "]";
		case HY_StateType.CREATE_ROLE:
			return "HY_GameRoleInfo [上传类型=" + "创建角色" + ", roleId=" + roleId
					+ ", roleName=" + roleName + ", roleLevel=" + roleLevel
					+ ", zoneId=" + zoneId + ", zoneName=" + zoneName
					+ ", balance=" + balance + ", vip=" + vip + ", partyName="
					+ partyName + "]";
		case HY_StateType.ENTER_SERVER:
			return "HY_GameRoleInfo [上传类型=" + "登录" + ", roleId=" + roleId
					+ ", roleName=" + roleName + ", roleLevel=" + roleLevel
					+ ", zoneId=" + zoneId + ", zoneName=" + zoneName
					+ ", balance=" + balance + ", vip=" + vip + ", partyName="
					+ partyName + "]";
		}
		return null;

	}
	 public GameRoleInfo getRoleInfo(Map<String, String> roleInfo)
	 {
		 GameRoleInfo HYU9GameRoleInfo = new GameRoleInfo();
		 if(roleInfo.get("TypeId")!=null){
			 HYU9GameRoleInfo.typeId = Integer.valueOf(roleInfo.get("TypeId"));
		 }
		 if(roleInfo.get("RoleId")!=null){
			 HYU9GameRoleInfo.roleId = roleInfo.get("RoleId");
		 }
		 if(roleInfo.get("RoleLevel")!=null){
			 HYU9GameRoleInfo.roleLevel = roleInfo.get("RoleLevel");
		 }
		 if(roleInfo.get("RoleName")!=null){
			 HYU9GameRoleInfo.roleName = roleInfo.get("RoleName");
		 }
		 if(roleInfo.get("ZoneId")!=null){
			 HYU9GameRoleInfo.zoneId = roleInfo.get("ZoneId");
		 }
		 if(roleInfo.get("ZoneName")!=null){
			 HYU9GameRoleInfo.zoneName = roleInfo.get("ZoneName");
		 }
		 if(roleInfo.get("PartyName")!=null){
			 HYU9GameRoleInfo.partyName = roleInfo.get("PartyName");
		 }
		 if(roleInfo.get("Balance")!=null){
			 HYU9GameRoleInfo.balance = Integer.valueOf(roleInfo.get("Balance"));
		 }
		 if(roleInfo.get("CreateTime")!=null){
			 HYU9GameRoleInfo.createTime = roleInfo.get("CreateTime");
		 }
		 if(roleInfo.get("Vip")!=null){
			 HYU9GameRoleInfo.vip = roleInfo.get("Vip");
		 }
		 return HYU9GameRoleInfo;
	 }

}
