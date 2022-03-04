from lib.connection.session import Session
from lib.service.admin.users import Users
from lib.utils.credentials import Credentials
from tests.utils.users import admin_creds


class AdminSession(Session):
    def __init__(self, credentials: Credentials = admin_creds):
        super().__init__(creds=credentials)

    @property
    def users(self):
        return Users(session=self)


def get_admin_session() -> AdminSession:
    return Session.admin_session


Admin = lambda: get_admin_session()
