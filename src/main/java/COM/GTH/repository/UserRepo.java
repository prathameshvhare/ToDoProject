package COM.GTH.repository;

import COM.GTH.model.UserModel;

public interface UserRepo {
	public boolean isAddUser(UserModel model);
	
	public boolean isValidUser(UserModel model);

	/** Returns the matching user with primary key set, or null if not found. */
	public UserModel findUserByCredentials(UserModel model);

}
