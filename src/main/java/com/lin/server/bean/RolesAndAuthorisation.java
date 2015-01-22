package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class RolesAndAuthorisation implements Serializable, Comparable<RolesAndAuthorisation>{

	@Id	private long id;
	@Index private String roleName;
	private String roleType;			// Built-In or Custom Role
	private String roleStatus;
	private String roleDescription;
	private String companyId;
	private long createdById;
	private long lastModifiedById;
	private Date creationDate;
    private Date lastModifiedDate;
	private String A1;
	private String A2;
	private String A3;
	private String A4;
	private String A5;
	private String A6;
	private String A7;
	private String A8;
	private String A9;
	private String A10;
	private String A11;
	private String A12;
	private String A13;
	private String A14;
	private String A15;
	private String A16;
	private String A17;
	private String A18;
	private String A19;
	private String A20;
	private String A21;
	private String A22;
	private String A23;
	private String A24;
	private String A25;
	private String A26;
	private String A27;
	private String A28;
	private String A29;
	private String A30;
	private String A31;
	private String A32;
	private String A33;
	private String A34;
	private String A35;
	private String A36;
	private String A37;
	private String A38;
	private String A39;
	private String A40;
	private String A41;
	private String A42;
	private String A43;
	private String A44;
	private String A45;
	private String A46;
	private String A47;
	private String A48;
	private String A49;
	private String A50;
	private String A51;
	private String A52;
	private String A53;
	private String A54;
	private String A55;
	private String A56;
	private String A57;
	private String A58;
	private String A59;
	private String A60;
	private String A61;
	private String A62;
	private String A63;
	private String A64;
	private String A65;
	private String A66;
	private String A67;
	private String A68;
	private String A69;
	private String A70;
	private String A71;
	private String A72;
	private String A73;
	private String A74;
	private String A75;
	private String A76;
	private String A77;
	private String A78;
	private String A79;
	private String A80;
	private String A81;
	private String A82;
	private String A83;
	private String A84;
	private String A85;
	private String A86;
	private String A87;
	private String A88;
	private String A89;
	private String A90;
	private String A91;
	private String A92;
	private String A93;
	private String A94;
	private String A95;
	private String A96;
	private String A97;
	private String A98;
	private String A99;
	private String A100;
	
	public RolesAndAuthorisation(){
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}

	public String getA1() {
		return A1;
	}

	public void setA1(String a1) {
		A1 = a1;
	}

	public String getA2() {
		return A2;
	}

	public void setA2(String a2) {
		A2 = a2;
	}

	public String getA3() {
		return A3;
	}

	public void setA3(String a3) {
		A3 = a3;
	}

	public String getA4() {
		return A4;
	}

	public void setA4(String a4) {
		A4 = a4;
	}

	public String getA5() {
		return A5;
	}

	public void setA5(String a5) {
		A5 = a5;
	}

	public String getA6() {
		return A6;
	}

	public void setA6(String a6) {
		A6 = a6;
	}

	public String getA7() {
		return A7;
	}

	public void setA7(String a7) {
		A7 = a7;
	}

	public String getA8() {
		return A8;
	}

	public void setA8(String a8) {
		A8 = a8;
	}

	public String getA9() {
		return A9;
	}

	public void setA9(String a9) {
		A9 = a9;
	}

	public String getA10() {
		return A10;
	}

	public void setA10(String a10) {
		A10 = a10;
	}

	public String getA11() {
		return A11;
	}

	public void setA11(String a11) {
		A11 = a11;
	}

	public String getA12() {
		return A12;
	}

	public void setA12(String a12) {
		A12 = a12;
	}

	public String getA13() {
		return A13;
	}

	public void setA13(String a13) {
		A13 = a13;
	}

	public String getA14() {
		return A14;
	}

	public void setA14(String a14) {
		A14 = a14;
	}

	public String getA15() {
		return A15;
	}

	public void setA15(String a15) {
		A15 = a15;
	}

	public String getA16() {
		return A16;
	}

	public void setA16(String a16) {
		A16 = a16;
	}

	public String getA17() {
		return A17;
	}

	public void setA17(String a17) {
		A17 = a17;
	}

	public String getA18() {
		return A18;
	}

	public void setA18(String a18) {
		A18 = a18;
	}

	public String getA19() {
		return A19;
	}

	public void setA19(String a19) {
		A19 = a19;
	}

	public String getA20() {
		return A20;
	}

	public void setA20(String a20) {
		A20 = a20;
	}

	public String getA21() {
		return A21;
	}

	public void setA21(String a21) {
		A21 = a21;
	}

	public String getA22() {
		return A22;
	}

	public void setA22(String a22) {
		A22 = a22;
	}

	public String getA23() {
		return A23;
	}

	public void setA23(String a23) {
		A23 = a23;
	}

	public String getA24() {
		return A24;
	}

	public void setA24(String a24) {
		A24 = a24;
	}

	public String getA25() {
		return A25;
	}

	public void setA25(String a25) {
		A25 = a25;
	}

	public String getA26() {
		return A26;
	}

	public void setA26(String a26) {
		A26 = a26;
	}

	public String getA27() {
		return A27;
	}

	public void setA27(String a27) {
		A27 = a27;
	}

	public String getA28() {
		return A28;
	}

	public void setA28(String a28) {
		A28 = a28;
	}

	public String getA29() {
		return A29;
	}

	public void setA29(String a29) {
		A29 = a29;
	}

	public String getA30() {
		return A30;
	}

	public void setA30(String a30) {
		A30 = a30;
	}

	public String getA31() {
		return A31;
	}

	public void setA31(String a31) {
		A31 = a31;
	}

	public String getA32() {
		return A32;
	}

	public void setA32(String a32) {
		A32 = a32;
	}

	public String getA33() {
		return A33;
	}

	public void setA33(String a33) {
		A33 = a33;
	}

	public String getA34() {
		return A34;
	}

	public void setA34(String a34) {
		A34 = a34;
	}

	public String getA35() {
		return A35;
	}

	public void setA35(String a35) {
		A35 = a35;
	}

	public String getA36() {
		return A36;
	}

	public void setA36(String a36) {
		A36 = a36;
	}

	public String getA37() {
		return A37;
	}

	public void setA37(String a37) {
		A37 = a37;
	}

	public String getA38() {
		return A38;
	}

	public void setA38(String a38) {
		A38 = a38;
	}

	public String getA39() {
		return A39;
	}

	public void setA39(String a39) {
		A39 = a39;
	}

	public String getA40() {
		return A40;
	}

	public void setA40(String a40) {
		A40 = a40;
	}

	public String getA41() {
		return A41;
	}

	public void setA41(String a41) {
		A41 = a41;
	}

	public String getA42() {
		return A42;
	}

	public void setA42(String a42) {
		A42 = a42;
	}

	public String getA43() {
		return A43;
	}

	public void setA43(String a43) {
		A43 = a43;
	}

	public String getA44() {
		return A44;
	}

	public void setA44(String a44) {
		A44 = a44;
	}

	public String getA45() {
		return A45;
	}

	public void setA45(String a45) {
		A45 = a45;
	}

	public String getA46() {
		return A46;
	}

	public void setA46(String a46) {
		A46 = a46;
	}

	public String getA47() {
		return A47;
	}

	public void setA47(String a47) {
		A47 = a47;
	}

	public String getA48() {
		return A48;
	}

	public void setA48(String a48) {
		A48 = a48;
	}

	public String getA49() {
		return A49;
	}

	public void setA49(String a49) {
		A49 = a49;
	}

	public String getA50() {
		return A50;
	}

	public void setA50(String a50) {
		A50 = a50;
	}

	public String getA51() {
		return A51;
	}

	public void setA51(String a51) {
		A51 = a51;
	}

	public String getA52() {
		return A52;
	}

	public void setA52(String a52) {
		A52 = a52;
	}

	public String getA53() {
		return A53;
	}

	public void setA53(String a53) {
		A53 = a53;
	}

	public String getA54() {
		return A54;
	}

	public void setA54(String a54) {
		A54 = a54;
	}

	public String getA55() {
		return A55;
	}

	public void setA55(String a55) {
		A55 = a55;
	}

	public String getA56() {
		return A56;
	}

	public void setA56(String a56) {
		A56 = a56;
	}

	public String getA57() {
		return A57;
	}

	public void setA57(String a57) {
		A57 = a57;
	}

	public String getA58() {
		return A58;
	}

	public void setA58(String a58) {
		A58 = a58;
	}

	public String getA59() {
		return A59;
	}

	public void setA59(String a59) {
		A59 = a59;
	}

	public String getA60() {
		return A60;
	}

	public void setA60(String a60) {
		A60 = a60;
	}

	public String getA61() {
		return A61;
	}

	public void setA61(String a61) {
		A61 = a61;
	}

	public String getA62() {
		return A62;
	}

	public void setA62(String a62) {
		A62 = a62;
	}

	public String getA63() {
		return A63;
	}

	public void setA63(String a63) {
		A63 = a63;
	}

	public String getA64() {
		return A64;
	}

	public void setA64(String a64) {
		A64 = a64;
	}

	public String getA65() {
		return A65;
	}

	public void setA65(String a65) {
		A65 = a65;
	}

	public String getA66() {
		return A66;
	}

	public void setA66(String a66) {
		A66 = a66;
	}

	public String getA67() {
		return A67;
	}

	public void setA67(String a67) {
		A67 = a67;
	}

	public String getA68() {
		return A68;
	}

	public void setA68(String a68) {
		A68 = a68;
	}

	public String getA69() {
		return A69;
	}

	public void setA69(String a69) {
		A69 = a69;
	}

	public String getA70() {
		return A70;
	}

	public void setA70(String a70) {
		A70 = a70;
	}

	public String getA71() {
		return A71;
	}

	public void setA71(String a71) {
		A71 = a71;
	}

	public String getA72() {
		return A72;
	}

	public void setA72(String a72) {
		A72 = a72;
	}

	public String getA73() {
		return A73;
	}

	public void setA73(String a73) {
		A73 = a73;
	}

	public String getA74() {
		return A74;
	}

	public void setA74(String a74) {
		A74 = a74;
	}

	public String getA75() {
		return A75;
	}

	public void setA75(String a75) {
		A75 = a75;
	}

	public String getA76() {
		return A76;
	}

	public void setA76(String a76) {
		A76 = a76;
	}

	public String getA77() {
		return A77;
	}

	public void setA77(String a77) {
		A77 = a77;
	}

	public String getA78() {
		return A78;
	}

	public void setA78(String a78) {
		A78 = a78;
	}

	public String getA79() {
		return A79;
	}

	public void setA79(String a79) {
		A79 = a79;
	}

	public String getA80() {
		return A80;
	}

	public void setA80(String a80) {
		A80 = a80;
	}

	public String getA81() {
		return A81;
	}

	public void setA81(String a81) {
		A81 = a81;
	}

	public String getA82() {
		return A82;
	}

	public void setA82(String a82) {
		A82 = a82;
	}

	public String getA83() {
		return A83;
	}

	public void setA83(String a83) {
		A83 = a83;
	}

	public String getA84() {
		return A84;
	}

	public void setA84(String a84) {
		A84 = a84;
	}

	public String getA85() {
		return A85;
	}

	public void setA85(String a85) {
		A85 = a85;
	}

	public String getA86() {
		return A86;
	}

	public void setA86(String a86) {
		A86 = a86;
	}

	public String getA87() {
		return A87;
	}

	public void setA87(String a87) {
		A87 = a87;
	}

	public String getA88() {
		return A88;
	}

	public void setA88(String a88) {
		A88 = a88;
	}

	public String getA89() {
		return A89;
	}

	public void setA89(String a89) {
		A89 = a89;
	}

	public String getA90() {
		return A90;
	}

	public void setA90(String a90) {
		A90 = a90;
	}

	public String getA91() {
		return A91;
	}

	public void setA91(String a91) {
		A91 = a91;
	}

	public String getA92() {
		return A92;
	}

	public void setA92(String a92) {
		A92 = a92;
	}

	public String getA93() {
		return A93;
	}

	public void setA93(String a93) {
		A93 = a93;
	}

	public String getA94() {
		return A94;
	}

	public void setA94(String a94) {
		A94 = a94;
	}

	public String getA95() {
		return A95;
	}

	public void setA95(String a95) {
		A95 = a95;
	}

	public String getA96() {
		return A96;
	}

	public void setA96(String a96) {
		A96 = a96;
	}

	public String getA97() {
		return A97;
	}

	public void setA97(String a97) {
		A97 = a97;
	}

	public String getA98() {
		return A98;
	}

	public void setA98(String a98) {
		A98 = a98;
	}

	public String getA99() {
		return A99;
	}

	public void setA99(String a99) {
		A99 = a99;
	}

	public String getA100() {
		return A100;
	}

	public void setA100(String a100) {
		A100 = a100;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setLastModifiedById(long lastModifiedById) {
		this.lastModifiedById = lastModifiedById;
	}

	public long getLastModifiedById() {
		return lastModifiedById;
	}

	public void setCreatedById(long createdById) {
		this.createdById = createdById;
	}

	public long getCreatedById() {
		return createdById;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyId() {
		return companyId;
	}

	@Override
	public int compareTo(RolesAndAuthorisation rolesAndAuthorisation) {
		return roleName.compareToIgnoreCase(rolesAndAuthorisation.getRoleName());
	}
	
	
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}		
	
		
}
