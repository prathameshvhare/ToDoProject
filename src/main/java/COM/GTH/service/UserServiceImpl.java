package COM.GTH.service;

import COM.GTH.model.UserModel;
import COM.GTH.repository.UserRepo;
import COM.GTH.repository.UserRepoImpl;

public class UserServiceImpl implements UserService {
	UserRepo userrepo=new UserRepoImpl();

	@Override
	public boolean isAddUser(UserModel model) {
		// TODO Auto-generated method stub
		return userrepo.isAddUser(model);
	}

	@Override
	public boolean isValidUser(UserModel model) {
		// TODO Auto-generated method stub
		return userrepo.isValidUser(model);
	}

}
