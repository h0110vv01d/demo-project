import asyncio
import json
import ssl
import sys
import threading
import websockets

from lib.connection.session import Session


class Websocket:
    def __init__(self, host=Session.host):
        self._ws = None
        self._host = host
        self._loop = asyncio.new_event_loop()
        self._stop_thread_event = threading.Event()
        self._stop_asyncio_event = asyncio.Event(loop=self._loop)
        self.events = dict()
        self._thread = threading.Thread(target=self.thread,
                                        args=(self._stop_thread_event,), daemon=True)
        self._thread.start()

    # thread
    def thread(self, stop_event):
        while not stop_event.is_set():
            self._loop.run_until_complete(self._daemon())
        tasks = asyncio.all_tasks(self._loop)
        for task in tasks:
            task.cancel()
        self._loop.stop()
        self._loop.close()

    # asyncio
    async def _connect(self):
        context = ssl.create_default_context()
        context.check_hostname = False
        # context.verify_mode = ssl.CERT_NONE
        url = f'ws://{self._host}:8080/app/hello'
        try:
            self._ws = await websockets.connect(url)
        except websockets.exceptions.InvalidStatusCode as ex:
            print(ex)


    async def _listen(self):
        while not self._stop_asyncio_event.is_set():
            print("sendin message")

            await self._ws.send(json.dumps({'text': 'hi'}))
            ret = await self._ws.recv()
            print("recieved"+ret)
        await self._ws.close()

    async def _daemon(self):
        await self._connect()
        await self._listen()

    def __del__(self):
        self._stop_asyncio_event.set()
        self._stop_thread_event.set()


if __name__ == '__main__':
    ws = Websocket()
    while True:
        pass