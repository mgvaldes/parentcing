package com.ing3nia.parentalcontrol.ui.utils;

import com.ing3nia.parentalcontrol.R;

public enum PCMapMarkersEnum {
	
	SMARTPHONE_A(0, R.drawable.marker_a),
	SMARTPHONE_B(1, R.drawable.marker_b),
	SMARTPHONE_C(2, R.drawable.marker_c),
	SMARTPHONE_D(3, R.drawable.marker_d),
	SMARTPHONE_E(4, R.drawable.marker_e),
	SMARTPHONE_F(5, R.drawable.marker_f),
	SMARTPHONE_G(6, R.drawable.marker_g),
	SMARTPHONE_H(7, R.drawable.marker_h),
	SMARTPHONE_I(8, R.drawable.marker_i),
	SMARTPHONE_J(9, R.drawable.marker_j),
	SMARTPHONE_K(10, R.drawable.marker_k),
	SMARTPHONE_L(11, R.drawable.marker_l),
	SMARTPHONE_M(12, R.drawable.marker_m),
	SMARTPHONE_N(13, R.drawable.marker_n),
	SMARTPHONE_O(14, R.drawable.marker_o),
	SMARTPHONE_P(15, R.drawable.marker_p),
	SMARTPHONE_Q(16, R.drawable.marker_q),
	SMARTPHONE_R(17, R.drawable.marker_r),
	SMARTPHONE_S(18, R.drawable.marker_s),
	SMARTPHONE_T(19, R.drawable.marker_t),
	SMARTPHONE_U(20, R.drawable.marker_u),
	SMARTPHONE_V(21, R.drawable.marker_v),
	SMARTPHONE_W(22, R.drawable.marker_w),
	SMARTPHONE_X(23, R.drawable.marker_x),
	SMARTPHONE_Y(23, R.drawable.marker_y),
	SMARTPHONE_Z(24, R.drawable.marker_z);

	Integer id;
	Integer imageName;
	
	PCMapMarkersEnum(Integer id, Integer imageName){
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
