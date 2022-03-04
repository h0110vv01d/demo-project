"""
https://www.keycloak.org/docs-api/15.0/rest-api/index.html#_realmrepresentation
"""


class RealmRepresentation:
    def __init__(self, name, enabled=True):
        self.name = name
        self.enabled = enabled

    def to_dict(self):
        return {"id": self.name,
                "displayName": self.name,
                "realm": self.name,
                "enabled": True}
