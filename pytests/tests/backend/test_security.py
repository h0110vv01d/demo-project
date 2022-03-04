from pytest import raises

from lib.exceptions import ForbiddenException
from lib.connection.admin_session import Admin


def test_token():
    """
    :return:
    """
    admin = Admin()
    token = admin.access_token

    admin.access_token = "Bearer qweqwe"
    # TODO valid token needed
    # with raises(ForbiddenException):
    #     admin.me()

    admin.access_token = token
    admin.me()
