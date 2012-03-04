package com.ing3nia.parentalcontrol.client.utils;

public enum LoadingBarImageEnum {

	STAGE1(1, "stage1.png"), STAGE2(2, "stage2.png"), STAGE3(3, "stage3.png");

	Integer id;
		String imageName;
		
		LoadingBarImageEnum(Integer id, String imageName){
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

