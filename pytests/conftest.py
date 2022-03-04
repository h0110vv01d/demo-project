import pytest

from lib.connection.admin_session import get_admin_session, AdminSession, Admin
from lib.connection.session import Session
from tests.utils.users import testuser1_creds


def pytest_addoption(parser):
    parser.addoption('--host', action='store',
                     default='localhost')
    parser.addoption('--init', action='store_true', default=False)


@pytest.mark.tryfirst
@pytest.fixture(scope='session', autouse=True)
def init(request):
    host = request.config.getoption("--host")
    Session.host = host
    Session.admin_session = AdminSession()
    if request.config.getoption('--init'):
        initialize()


def initialize():
    adm = get_admin_session()
    # if adm.user.search(testuser1.login, raise_timeout=False) is None:
    #     adm.user.create(testuser1)
