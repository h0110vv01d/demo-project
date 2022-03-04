import logging

from keycloak import KeycloakAdmin, KeycloakGetError

from lib.settings import USER_ROLE_NAME, ADMIN_ROLE_NAME


class CKARoles:
    def __init__(self, admin: KeycloakAdmin):
        self.admin = admin

    def is_role_exists(self, role):
        try:
            self.admin.get_realm_role(role)
            logging.info(f"role {role} already exists")
            return True
        except KeycloakGetError:
            return False

    def create_roles(self):
        for role in [ADMIN_ROLE_NAME, USER_ROLE_NAME]:
            if not self.is_role_exists(role):
                logging.info(f"creating role {role}")
                self.admin.create_realm_role({"name": role})
