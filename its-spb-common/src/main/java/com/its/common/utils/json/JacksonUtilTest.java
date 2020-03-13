package com.its.common.utils.json;

/**
 * 
 * @author tzz
 */
public class JacksonUtilTest {

    static class User {
        private Long id;
        private String name;
        private String password;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static void main(String[] args) {
		try {
            JacksonUtilTest.User us = new JacksonUtilTest.User();
			us.setId(123L);
			us.setName("aa");
			us.setPassword("cc");
			//bean转Json
			String json = JacksonUtil.nonDefaultMapper().toJson(us);
			System.out.println(json);
			json = "{\"id\":123,\"name\":\"aa\",\"password\":\"cc\"}"; 
			//Json转bean
			User u = JacksonUtil.nonDefaultMapper().fromJson(json, User.class);
			System.out.println(u.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
