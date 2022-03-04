class PageRequest:
    def __init__(self, page, size, filter, sort):
        self.page = page
        self.size = size
        self.filter = filter
        self.sort = sort

    def generate_filter(self):
        out = self.filter if self.filter is not None else []
        return out

    def generate_sort(self):
        if self.sort is not None:
            if ":" in self.sort:
                return self.sort
            else:
                return f"{self.sort}:ASC"
        else:
            return ""

    def get_data(self):
        return {'page': self.page,
                'size': self.size,
                'filter': self.generate_filter(),
                'sort': self.generate_sort()}


def get_page_request(page=0, size=100, filter=None, sort=None):
    page_request = _get_page_request(page, size, filter, sort)
    return page_request.get_data()


def _get_page_request(page, size, filter, sort):
    return PageRequest(page, size, filter, sort)
