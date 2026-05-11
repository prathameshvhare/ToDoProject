package COM.GTH.repository;

import COM.GTH.model.UserModel;

public class UserRepoImpl extends DBConfig implements UserRepo {

	@Override
	public boolean isAddUser(UserModel model) {
		try {
			stmt=conn.prepareStatement("insert into User values (0,?,?,?)");
			stmt.setString(1, model.getName());
			stmt.setString(2, model.getEmail());
			stmt.setString(3, model.getPassword());
			
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

}
