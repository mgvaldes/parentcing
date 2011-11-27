package com.ing3nia.parentalcontrol.client.utils;

public enum PCSmartphoneIconsEnum {
	
	A(0,"iconA.png"),
	B(1,"iconB.png"),
	C(2,"iconC.png"),
	D(3,"iconD.png"),
	E(4,"iconE.png"),
	F(5,"iconF.png"),
	G(6,"iconG.png"),
	H(7,"iconH.png"),
	I(8,"iconI.png"),
	J(9,"iconJ.png"),
	K(10,"iconK.png"),
	L(11,"iconL.png"),
	M(12,"iconM.png"),
	N(13,"iconN.png"),
	O(14,"iconO.png"),
	P(15,"iconP.png"),
	Q(16,"iconQ.png"),
	R(17,"iconR.png"),
	S(18,"iconS.png"),
	T(19,"iconT.png"),
	U(20,"iconU.png"),
	V(21,"iconV.png"),
	W(22,"iconW.png"),
	X(23,"iconX.png"),
	Y(23,"iconY.png"),
	Z(24,"iconZ.png");

	Integer id;
	String imageName;
	
	PCSmartphoneIconsEnum(Integer id, String imageName){
		this.id = id;
		this.imageName = imageName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}