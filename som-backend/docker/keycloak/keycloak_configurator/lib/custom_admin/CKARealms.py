import logging

from keycloak import KeycloakAdmin

from lib.settings import SOM_REALM_NAME, som_realm


class CKARealms:
    def __init__(self, admin: KeycloakAdmin):
        self.admin = admin

    def is_som_realm_exists(self):
        realms = self.admin.get_realms()
        for r in realms:
            if r.get("id") == SOM_REALM_NAME:
                logging.info(f"{SOM_REALM_NAME} already exist")
                return True
        return False

    def create_som_realm(self):
        if not self.is_som_realm_exists():
            logging.info(f"creating {SOM_REALM_NAME}")
            self.admin.create_realm(payload=som_realm.to_dict())

    def clean_realms(self, clean_all=False):
        if clean_all:
            for r in self.admin.get_realms():
                r_id = r.get("id")
                if r_id != "Master":
                    self.admin.delete_realm(r.get("id"))
        else:
            logging.info(f"deleting {SOM_REALM_NAME}")
            self.admin.delete_realm(SOM_REALM_NAME)
