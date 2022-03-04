from keycloak import KeycloakAdmin

from lib.custom_admin.CKAClients import CKAClients
from lib.custom_admin.CKARealms import CKARealms
from lib.custom_admin.CKARoles import CKARoles
from lib.custom_admin.CKAUsers import CKAUsers
from lib.settings import DEFAULT_SERVER_URL, ADMIN_CREDS, SOM_REALM_NAME


class CustomKeycloakAdmin:
    def __init__(self, admin: KeycloakAdmin):
        self.admin = admin

    def realms(self) -> CKARealms:
        return CKARealms(self.admin)

    def clients(self) -> CKAClients:
        return CKAClients(self.admin)

    def roles(self) -> CKARoles:
        return CKARoles(self.admin)

    def users(self) -> CKAUsers:
        return CKAUsers(self.admin)

    def configure(self):
        self.realms().create_som_realm()
        self.admin.realm_name = SOM_REALM_NAME
        self.clients().create_clients()
        self.roles().create_roles()
        self.users().create_admin()
        self.clients().log_clients_secret()


keycloak_admin = CustomKeycloakAdmin(KeycloakAdmin(server_url=f"{DEFAULT_SERVER_URL}/auth/",
                                                   username=ADMIN_CREDS.username,
                                                   password=ADMIN_CREDS.password,
                                                   realm_name="master",
                                                   verify=True))
