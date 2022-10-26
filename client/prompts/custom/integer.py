
from prompts.base import Prompt

from typing import Any, Iterable, Mapping

class IntPrompt(Prompt) :
	
	def __init__(self, data: "Mapping[str,Any]") :
		self.message: str = data['message']
		self.min: int = data['min']
		self.max: int = data['max']

	def print(self) :
		print(f"{self.message} ({self.min}..{self.max}) > ", end="")
	
	def accept(self, response: str) -> "int | str | Iterable[Any] | Mapping[str,Any] | None" :
		try :
			res = int(response)
			if res < self.min or res > self.max :
				return None
			return res
		except ValueError :
			return None

