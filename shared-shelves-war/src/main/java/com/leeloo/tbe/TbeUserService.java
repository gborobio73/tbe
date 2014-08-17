package com.leeloo.tbe;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class TbeUserService {
	private UserService userService = UserServiceFactory.getUserService();
	
	public TbeUser getCurrentUser()
	{		
		User user = userService.getCurrentUser();
    	return new TbeUser(user.getUserId(), user.getNickname(), user.getEmail());
	}

	public boolean isUserLoggedIn() {
		return userService.isUserLoggedIn();
	}
}
