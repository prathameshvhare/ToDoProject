package COM.GTH.repository;

import COM.GTH.model.UserModel;

public interface UserRepo {
	public boolean isAddUser(UserModel model);
	
	public boolean isValidUser(UserModel model);

}
