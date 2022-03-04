from tests.utils.settings import default_domain


class Credentials(object):
    def __init__(self, login=None, password='123qwe',
                 email=None,
                 first_name="default_first_name",
                 last_name="default_last_name"):
        self.login = login or email
        self.password = password
        self.email = email if email is not None else f"{login}@{default_domain}"
        self.first_name = first_name
        self.last_name = last_name

    def copy(self):
        return Credentials(self.login, self.password, self.email,
                           self.first_name, self.last_name)

    def __repr__(self):
        return f'{self.__class__.__name__} {self.email}'


if __name__ == '__main__':
    c = Credentials()
    print(c)
