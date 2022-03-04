import json

import requests

from lib.exceptions import exceptions, ApiException
from lib.settings import USERS_CLIENT_ID, USERS_CLIENT_SECRET
from lib.utils.credentials import Credentials
from lib.utils.page_request import PageRequest, get_page_request


class Session(requests.Session):
    host = 'localhost'
    protocol = 'http://'
    admin_session = None

    def __init__(self, creds: Credentials = None, session=None):
        self.creds = None
        self.access_token = None
        self.token = None
        super().__init__()
        if isinstance(session, Session):
            self.credentials = session.creds
            self.cookies = session.cookies
            self.token = session.token
            self.access_token = session.access_token
            # self.current_user = session.current_user
        else:
            self.creds = creds
            self.login()

    def login(self):
        data = {"username": self.creds.login,
                "password": self.creds.password,
                "clientId": USERS_CLIENT_ID,
                "clientSecret": USERS_CLIENT_SECRET}
        self.token = self.request_("post", "/auth/token", data=data)
        self.access_token = self.token["accessToken"]

    def request_(self, method, url, data=None, params=None,
                 json_body=True, stream=None, return_resp=False, files=None):
        url = f"{self.protocol}{self.host}{url}"
        bearer = None if self.access_token is None else f"bearer {self.access_token}"
        headers = {"Authorization": bearer}
        if json_body:
            data = json.dumps(data)
            headers['Content-Type'] = 'application/json;charset=utf-8'
        elif files is not None:
            pass
        else:
            # data = json.dumps(data)
            headers['Content-Type'] = 'application/x-www-form-urlencoded'

        response = self.request(method, url, data=data, params=params,
                                headers=headers, json=json_body, verify=False,
                                stream=stream, allow_redirects=False, files=files)
        check_response(response)
        if return_resp:
            return response
        try:
            out = response.json()
        except json.decoder.JSONDecodeError:
            out = response.text
        return out

    def paged(self, url, page_request: PageRequest = None):
        data = page_request if page_request is not None else get_page_request()
        response = self.request_('post', f"{url}/paged", data=data)['items']
        return response

    def me(self):
        url = "/auth/me"
        return self.request_("get", url)


def check_response(response):
    status_code = response.status_code
    if status_code >= 400:
        exception = exceptions.get(status_code, ApiException)
        raise exception(response)
