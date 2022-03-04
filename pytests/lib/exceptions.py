from requests import Response


class ApiException(Exception):

    def __init__(self, response: Response):
        super().__init__()
        self.resp = response
        self.status_code = response.status_code
        self.text = self.resp.text
        self.request = response.request
        self.url = self.request.url
        self.method = self.request.method
        self.body = self.request.body

    def __str__(self):
        # message = f"\n{self.__class__.__name__} \n"
        message = f"\n{self.method} - {self.url} - {self.status_code}\n"
        message += f"{self.text}\n"
        message += f"request body: {self.body}"
        return message


class BadRequestException(ApiException):
    """400 BadRequest"""


class AuthException(ApiException):
    """401 Auth"""


class ForbiddenException(ApiException):
    """403 Forbidden"""


class NotFoundException(ApiException):
    """404 NotFound"""


class ServerErrorException(ApiException):
    """500 ServerError"""


exceptions = {400: BadRequestException,
              401: ApiException,
              403: ForbiddenException,
              404: NotFoundException,
              500: ServerErrorException}
