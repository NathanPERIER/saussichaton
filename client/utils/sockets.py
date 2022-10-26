
import json
import socket

from typing import Iterable, Mapping, Any


class Socket :

	def __init__(self, host: str, port: int) :
		self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self.host = host
		self.port = port

	def __enter__(self) -> "Socket" :
		self.s.connect((self.host, self.port))
		return self
	
	def __exit__(self, exc_type, exc_value, exc_traceback) :
		self.s.__exit__()
		if exc_type is not None :
			return False
	
	def recv(self) -> "Mapping[str,Any]" :
		try :
			text = self._recv_partial()
			while not text.endswith('\n') :
				text += self._recv_partial()
		except ConnectionClosed :
			return {'type': 'disconnected'}
		return json.loads(text)
	
	def _recv_partial(self) -> str :
		data = self.s.recv(1024)
		if len(data) == 0 :
			raise ConnectionClosed()
		return data.decode('utf-8')
	
	def send(self, data: "int | str | Iterable[Any] | Mapping[str,Any]") :
		text = json.dumps(data) + "\n"
		data_bytes = text.encode('utf-8')
		self.s.sendall(data_bytes)
	
	def interrupted(self) :
		self.s.sendall(b'INTERRUPTED\n')


class ConnectionClosed(Exception) :
	pass
