from lib.utils.credentials import Credentials

testusers_creds = [Credentials(login=f'testuser{i}') for i in range(1, 6)]
testuser1_creds = testusers_creds[0]
admin_creds = Credentials(login='som_admin', password='123qwe')
