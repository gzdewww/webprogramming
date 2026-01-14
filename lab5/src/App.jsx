import { useState } from "react";
import "./App.css";
import Calculator from "./components/Calculator/Calculator";
import Result from "./components/Result/Result";

function App() {
  const [showCalculator, setShowCalculator] = useState(true);

  const [args, setArgs] = useState(['', '']);
  const [result, setResult] = useState("");
  const [operation, setOperation] = useState("+");
  const handleCalc = () => {
    if (operation === "/" && +args[1] === 0) {
      alert("На ноль делить нельзя");
      return;
    }
    if (args[0] === "" || args[1] === "") {
      alert("Введите аргументы");
      return;
    } 
    setResult(eval(`${args[0]} ${operation} ${args[1]}`));
    setShowCalculator(false);
  };

  return (
    <>
      {showCalculator ? (
        <Calculator
          args={args}
          setArgs={setArgs}
          result={result}
          operation={operation}
          setOperation={setOperation}
          handleCalc={handleCalc}
        />
      ) : (
        <Result
          args={args}
          operation={operation}
          result={result}
          onClick={() => setShowCalculator(true)}
        />
      )}
    </>
  );
}

export default App;
