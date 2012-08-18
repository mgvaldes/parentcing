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
	Z(24,"markerZ.png"),
	M001(1,"001.png"),
	M002(2,"002.png"),
	M003(3,"003.png"),
	M004(4,"004.png"),
	M005(5,"005.png"),
	M006(6,"006.png"),
	M007(7,"007.png"),
	M008(8,"008.png"),
	M009(9,"009.png"),
	M010(10,"010.png"),
	M011(11,"011.png"),
	M012(12,"012.png"),
	M013(13,"013.png"),
	M014(14,"014.png"),
	M015(15,"015.png"),
	M016(16,"016.png"),
	M017(17,"017.png"),
	M018(18,"018.png"),
	M019(19,"019.png"),
	M020(20,"020.png"),
	M021(21,"021.png"),
	M022(22,"022.png"),
	M023(23,"023.png"),
	M024(24,"024.png"),
	M025(25,"025.png"),
	M026(26,"026.png"),
	M027(27,"027.png"),
	M028(28,"028.png"),
	M029(29,"029.png"),
	M030(30,"030.png"),
	M031(31,"031.png"),
	M032(32,"032.png"),
	M033(33,"033.png"),
	M034(34,"034.png"),
	M035(35,"035.png"),
	M036(36,"036.png"),
	M037(37,"037.png"),
	M038(38,"038.png"),
	M039(39,"039.png"),
	M040(40,"040.png"),
	M041(41,"041.png"),
	M042(42,"042.png"),
	M043(43,"043.png"),
	M044(44,"044.png"),
	M045(45,"045.png"),
	M046(46,"046.png"),
	M047(47,"047.png"),
	M048(48,"048.png"),
	M049(49,"049.png"),
	M050(50,"050.png"),
	M051(51,"051.png"),
	M052(52,"052.png"),
	M053(53,"053.png"),
	M054(54,"054.png"),
	M055(55,"055.png"),
	M056(56,"056.png"),
	M057(57,"057.png"),
	M058(58,"058.png"),
	M059(59,"059.png"),
	M060(60,"060.png"),
	M061(61,"061.png"),
	M062(62,"062.png"),
	M063(63,"063.png"),
	M064(64,"064.png"),
	M065(65,"065.png"),
	M066(66,"066.png"),
	M067(67,"067.png"),
	M068(68,"068.png"),
	M069(69,"069.png"),
	M070(70,"070.png"),
	M071(71,"071.png"),
	M072(72,"072.png"),
	M073(73,"073.png"),
	M074(74,"074.png"),
	M075(75,"075.png"),
	M076(76,"076.png"),
	M077(77,"077.png"),
	M078(78,"078.png"),
	M079(79,"079.png"),
	M080(80,"080.png"),
	M081(81,"081.png"),
	M082(82,"082.png"),
	M083(83,"083.png"),
	M084(84,"084.png"),
	M085(85,"085.png"),
	M086(86,"086.png"),
	M087(87,"087.png"),
	M088(88,"088.png"),
	M089(89,"089.png"),
	M090(90,"090.png"),
	M091(91,"091.png"),
	M092(92,"092.png"),
	M093(93,"093.png"),
	M094(94,"094.png"),
	M095(95,"095.png"),
	M096(96,"096.png"),
	M097(97,"097.png"),
	M098(98,"098.png"),
	M099(99,"099.png"),
	M100(100,"100.png");

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
