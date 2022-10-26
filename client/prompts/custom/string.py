
from prompts.base import Prompt

from typing import Any, Iterable, Mapping

class StringPrompt(Prompt) :
	
	def __init__(self, data: "Mapping[str,Any]") :
		self.message: str = data['message']

	def print(self) :
		print(f"{self.message}\n > ", end="")
	
	def accept(self, response: str) -> "int | str | Iterable[Any] | Mapping[str,Any] | None" :
		if len(response) == 0 :
			return None
		return response
