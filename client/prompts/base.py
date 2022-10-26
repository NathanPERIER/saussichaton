
from typing import Any, Iterable, Mapping

class Prompt :

	def __init__(self, _data: "Mapping[str,Any]") :
		pass

	def print(self) :
		raise NotImplementedError()
	
	def accept(self, response: str) -> "int | bool | str | Iterable[Any] | Mapping[str,Any] | None" :
		raise NotImplementedError()
