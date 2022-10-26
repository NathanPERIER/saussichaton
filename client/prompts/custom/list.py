
from prompts.base import Prompt

from typing import Any, Iterable, Sequence, Mapping

class ListPrompt(Prompt) :
	
	def __init__(self, data: "Mapping[str,Any]") :
		self.message: str = data['message']
		self.options: "Sequence[str]" = data['options']
		self.available: "Sequence[bool]" = data['available']

	def print(self) :
		print(self.message)
		i = 0
		for opt, avl in zip(self.options, self.available) :
			i += 1
			if(avl) :
				print(f"{i}. {opt}")
			else :
				print(f"\033[30;1m{i}. {opt}\033[0m")
		print('> ', end='')
	
	def accept(self, response: str) -> "int | str | Iterable[Any] | Mapping[str,Any] | None" :
		try :
			res = int(response)
			if res <= 0 or res > len(self.options) :
				return None
			return res
		except ValueError :
			return None
