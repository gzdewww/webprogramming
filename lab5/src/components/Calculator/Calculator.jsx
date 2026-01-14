import "./Calculator.scss";
import { FaCross, FaDivide, FaMinus, FaPlus } from "react-icons/fa";
import { FaX, FaXmark } from "react-icons/fa6";

export default function Calculator({
  args,
  setArgs,
  operation,
  setOperation,
  handleCalc,
}) {
  return (
    <div className="calculator-wrapper">
      <h1>Калькулятор</h1>
      <p>Введите аргументы для вычисления</p>
      <input
        className="argInput"
        type="text"
        value={args[0]}
        placeholder="Введите аргумент"
        onChange={(e) => setArgs([e.target.value.replace(/\D/g, ""), args[1]])}
      />
      <input
        className="argInput"
        type="text"
        value={args[1]}
        placeholder="Введите аргумент"
        onChange={(e) => {
          setArgs([args[0], e.target.value.replace(/\D/g, "")]);
        }}
      />

      <div className="options">
        <label htmlFor="add">
          <input
            type="radio"
            name="operation"
            id="add"
            value="+"
            checked={operation === "+"}
            onChange={(e) => setOperation(e.target.value)}
          />
          <FaPlus />
        </label>
        <label htmlFor="substract">
          <input
            type="radio"
            name="operation"
            id="substract"
            value="-"
            checked={operation === "-"}
            onChange={(e) => setOperation(e.target.value)}
          />
          <FaMinus />
        </label>
        <label htmlFor="multiply">
          <input
            type="radio"
            name="operation"
            id="multiply"
            value="*"
            checked={operation === "*"}
            onChange={(e) => setOperation(e.target.value)}
          />
          <FaXmark />
        </label>
        <label htmlFor="divide">
          <input
            type="radio"
            name="operation"
            id="divide"
            value="/"
            checked={operation === "/"}
            onChange={(e) => setOperation(e.target.value)}
          />
          <FaDivide />
        </label>
      </div>
      <button type="button" onClick={handleCalc}>
        Рассчитать
      </button>
    </div>
  );
}
