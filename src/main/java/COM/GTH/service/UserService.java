package COM.GTH.service;

import COM.GTH.model.UserModel;

public interface UserService {
	
	public boolean isAddUser(UserModel model);
	public boolean isValidUser(UserModel model);

	public UserModel findUserByCredentials(UserModel model);

}
