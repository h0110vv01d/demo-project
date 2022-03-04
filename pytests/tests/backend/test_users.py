from lib.connection.session import Session
from lib.model.user.user_type import UserType
from lib.utils.page_request import get_page_request
from tests.utils.cleanup_type import CleanupType
from tests.utils.fixtures import clean_before
from tests.utils.names import users_with_names
from lib.connection.admin_session import Admin
from tests.utils.users import testusers_creds


@clean_before([CleanupType.USERS])
def test_page_request():
    """
    :return:
    """
    admin = Admin()
    for user in users_with_names:
        admin.users.create(user)
    name = 'lena'
    user = admin.users.paged(page_request=get_page_request(filter=[f"login:like:{name}"]))[0]
    assert name in user.login


@clean_before([CleanupType.USERS])
def test_create_users():
    """
    :return:
    """
    admin = Admin()
    for user_creds in testusers_creds[1:]:
        admin.users.create(user_creds)
        user = admin.users.search(user_creds.login)
        assert user.login == user.login
        sess = Session(user_creds)
        assert sess.me() == UserType.LOCAL.name
        user.delete()
        assert admin.users.search(user.login, raise_timeout=False) is None

