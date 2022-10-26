
from prompts.base import Prompt

from typing import Any, Iterable, Sequence, Mapping

class MultiListPrompt(Prompt) :
	
	def __init__(self, data: "Mapping[str,Any]") :
		self.message: str = data['message']
		self.options: "Sequence[str]" = data['options']
		self.available: "Sequence[bool]" = data['available']
		self.skip: "str | None" = data['none_option']

	def print(self) :
		print(self.message)
		i = 0
		for opt, avl in zip(self.options, self.available) :
			i += 1
			if(avl) :
				print(f"{i}. {opt}")
			else :
				print(f"\033[30;1m{i}. {opt}\033[0m")
		if self.skip is not None :
			print(f"{i+1}. {self.skip}")
		print('> ', end='')
	
	def accept(self, response: str) -> "int | str | Iterable[Any] | Mapping[str,Any] | None" :
		try :
			res = [int(x)-1 for x in response.split()]
		except ValueError :
			return None
		if len(res) == 0 :
			return None
		if len(res) == 1 and self.skip is not None and res[0] == len(self.options) :
			return res
		for x in res :
			if x < 0 or x >= len(self.options) or not self.available[x] :
				print('nope')
				return None
			return res
