package com.ing3nia.parentalcontrol.client.utils;

public enum PCMapMarkersEnum {
	
	A(0,"markerA.png"),
	B(1,"markerB.png"),
	C(2,"markerC.png"),
	D(3,"markerD.png"),
	E(4,"markerE.png"),
	F(5,"markerF.png"),
	G(6,"markerG.png"),
	H(7,"markerH.png"),
	I(8,"markerI.png"),
	J(9,"markerJ.png"),
	K(10,"markerK.png"),
	L(11,"markerL.png"),
	M(12,"markerM.png"),
	N(13,"markerN.png"),
	O(14,"markerO.png"),
	P(15,"markerP.png"),
	Q(16,"markerQ.png"),
	R(17,"markerR.png"),
	S(18,"markerS.png"),
	T(19,"markerT.png"),
	U(20,"markerU.png"),
	V(21,"markerV.png"),
	W(22,"markerW.png"),
	X(23,"markerX.png"),
	Y(24,"markerY.png"),
	Z(24,"markerZ.png");

	Integer id;
	String imageName;
	
	PCMapMarkersEnum(Integer id, String imageName){
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
