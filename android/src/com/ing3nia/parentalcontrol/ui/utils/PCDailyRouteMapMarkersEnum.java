package com.ing3nia.parentalcontrol.ui.utils;

import com.ing3nia.parentalcontrol.R;

public enum PCDailyRouteMapMarkersEnum {
	
	SMARTPHONE_A(0, R.drawable.darkgreen_marker_a),
	SMARTPHONE_B(1, R.drawable.darkgreen_marker_b),
	SMARTPHONE_C(2, R.drawable.darkgreen_marker_c),
	SMARTPHONE_D(3, R.drawable.darkgreen_marker_d),
	SMARTPHONE_E(4, R.drawable.darkgreen_marker_e),
	SMARTPHONE_F(5, R.drawable.darkgreen_marker_f),
	SMARTPHONE_G(6, R.drawable.darkgreen_marker_g),
	SMARTPHONE_H(7, R.drawable.darkgreen_marker_h),
	SMARTPHONE_I(8, R.drawable.darkgreen_marker_i),
	SMARTPHONE_J(9, R.drawable.darkgreen_marker_j),
	SMARTPHONE_K(10, R.drawable.darkgreen_marker_k),
	SMARTPHONE_L(11, R.drawable.darkgreen_marker_l),
	SMARTPHONE_M(12, R.drawable.darkgreen_marker_m),
	SMARTPHONE_N(13, R.drawable.darkgreen_marker_n),
	SMARTPHONE_O(14, R.drawable.darkgreen_marker_o),
	SMARTPHONE_P(15, R.drawable.darkgreen_marker_p),
	SMARTPHONE_Q(16, R.drawable.darkgreen_marker_q),
	SMARTPHONE_R(17, R.drawable.darkgreen_marker_r),
	SMARTPHONE_S(18, R.drawable.darkgreen_marker_s),
	SMARTPHONE_T(19, R.drawable.darkgreen_marker_t),
	SMARTPHONE_U(20, R.drawable.darkgreen_marker_u),
	SMARTPHONE_V(21, R.drawable.darkgreen_marker_v),
	SMARTPHONE_W(22, R.drawable.darkgreen_marker_w),
	SMARTPHONE_X(23, R.drawable.darkgreen_marker_x),
	SMARTPHONE_Y(23, R.drawable.darkgreen_marker_y),
	SMARTPHONE_Z(24, R.drawable.darkgreen_marker_z);

	Integer id;
	Integer imageName;
	
	PCDailyRouteMapMarkersEnum(Integer id, Integer imageName){
		this.id = id;
		this.imageName = imageName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getImageName() {
		return imageName;
	}

	public void setImageName(Integer imageName) {
		this.imageName = imageName;
	}
}
