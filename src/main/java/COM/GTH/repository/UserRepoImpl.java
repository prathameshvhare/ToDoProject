package COM.GTH.repository;

import java.sql.ResultSetMetaData;

import COM.GTH.model.UserModel;

public class UserRepoImpl extends DBConfig implements UserRepo {

	@Override
	public boolean isAddUser(UserModel model) {
		try {
			stmt=conn.prepareStatement("INSERT INTO User (name, email, password, mobile) VALUES (?, ?, ?, ?)");
			stmt.setString(1, model.getName());
			stmt.setString(2, model.getEmail());
			stmt.setString(3, model.getPassword());
			stmt.setString(4, model.getMobileNo());
			
			
			int value=stmt.executeUpdate();
			return value > 0?true:false;
			
		}
		catch(Exception ex) {
			System.out.println("Error is:" + ex);
			
		}
		
		return false;
	}

	@Override
	public boolean isValidUser(UserModel model) {
		
		try {
			stmt=conn.prepareStatement("select * from User where email=? and password=?");
			stmt.setString(1, model.getEmail());
			stmt.setString(2, model.getPassword());
			
			rs=stmt.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			else {
				return false;
			}
			
			
		}
		catch(Exception ex) {
			System.out.println("Error is:" + ex);
			
		}
		return false;
	}

	@Override
	public UserModel findUserByCredentials(UserModel model) {
		try {
			stmt = conn.prepareStatement("SELECT * FROM User WHERE email=? AND password=?");
			stmt.setString(1, model.getEmail());
			stmt.setString(2, model.getPassword());
			rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			UserModel out = new UserModel();
			out.setEmail(rs.getString("email"));
			out.setPassword(rs.getString("password"));
			out.setName(rs.getString("name"));
			out.setMobileNo(rs.getString("mobile"));
			int userPk = readUserPrimaryKey(rs);
			out.setUser_id(userPk);
			return out;
		} catch (Exception ex) {
			System.out.println("Error is:" + ex);
		}
		return null;
	}

	private static int readUserPrimaryKey(java.sql.ResultSet rs) throws Exception {
		ResultSetMetaData meta = rs.getMetaData();
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			String label = meta.getColumnLabel(i);
			if (label == null) {
				continue;
			}
			String lower = label.toLowerCase();
			if ("user_id".equals(lower) || "userid".equals(lower) || "id".equals(lower)) {
				return rs.getInt(i);
			}
		}
		try {
			return rs.getInt("user_id");
		} catch (Exception ignored) {
			return rs.getInt("id");
		}
	}

}
