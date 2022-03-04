from typing import List

from lib.connection.session import Session
from lib.model.user.user import User
from lib.model.user.user_type import UserType
from lib.utils.credentials import Credentials
from lib.utils.fixtures import repeat
from lib.utils.page_request import get_page_request


class Users(Session):
    url = '/users'

    def create(self, user: Credentials, user_type=UserType.LOCAL.value) -> User:
        data = {'login': user.login,
                'password': user.password,
                'userRole': user_type}
        url = f"{self.url}/create"
        resp = self.request_('post', url, data=data)
        return User(self, resp)

    def paged(self, url=None, page_request=None) -> List[User]:
        resp = super(Users, self).paged(self.url, page_request)
        users = [User(self, item) for item in resp]
        return users

    @repeat()
    def search(self, login, raise_timeout=True) -> User:
        page_request = get_page_request(filter=[f'login:like:{login}'])
        users = self.paged(page_request=page_request)
        if len(users) > 0:
            for user in users:
                if login in user.login:
                    return user
        if raise_timeout:
            raise TimeoutError(f'user {login} not found')
