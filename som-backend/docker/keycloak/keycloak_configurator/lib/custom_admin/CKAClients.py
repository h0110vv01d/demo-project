import logging

from keycloak import KeycloakAdmin

from lib.custom_admin.payloads import SOM_CORE_SPRING_BOOT_ADMIN_CLIENT_CREATE_PAYLOAD, \
    SOM_CORE_USERS_CLIENT_CREATE_PAYLOAD
from lib.settings import SOM_CORE_SPRING_BOOT_ADMIN_CLIENT_ID, SOM_CORE_USERS_CLIENT_ID


class CKAClients:
    def __init__(self, admin: KeycloakAdmin):
        self.admin = admin

    def clean_clients(self):
        for c in self.admin.get_clients():
            client_id = c.get("clientId")
            if client_id == SOM_CORE_SPRING_BOOT_ADMIN_CLIENT_ID:
                self.admin.delete_client(client_id)

    def is_client_exist(self, client_id):
        for c in self.admin.get_clients():
            found_id = c.get("clientId")
            if found_id == client_id:
                return True
        return False

    def create_clients(self):
        clients = {SOM_CORE_SPRING_BOOT_ADMIN_CLIENT_ID: SOM_CORE_SPRING_BOOT_ADMIN_CLIENT_CREATE_PAYLOAD,
                   SOM_CORE_USERS_CLIENT_ID: SOM_CORE_USERS_CLIENT_CREATE_PAYLOAD}
        for c in clients.keys():
            if self.is_client_exist(c):
                logging.info(f"client {c} already exists")
                continue
            logging.info(f"creating client {c}")
            self.admin.create_client(clients[c])

    def get_secret(self, id):
        for c in self.admin.get_clients():
            if c.get("clientId") == id:
                client_id = c.get("id")
                return self.admin.get_client_secrets(client_id)
        return None

    def log_clients_secret(self):
        secret = self.get_secret(SOM_CORE_SPRING_BOOT_ADMIN_CLIENT_ID).get("value")
        logging.info(f"{SOM_CORE_SPRING_BOOT_ADMIN_CLIENT_ID} secret:{secret}")
        secret = self.get_secret(SOM_CORE_USERS_CLIENT_ID).get("value")
        logging.info(f"{SOM_CORE_USERS_CLIENT_ID} secret:{secret}")