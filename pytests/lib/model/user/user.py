class User:
    url = '/users'

    def __init__(self, session, data):
        self.session = session
        self.id = data['id']
        self.login = data['login']

    def update(self):
        return self

    def delete(self):
        resp = self.session.request_('delete', f"{self.url}/delete/{self.id}")
        return resp

    def __repr__(self):
        return f"{self.__class__.__name__} - {self.login}"
