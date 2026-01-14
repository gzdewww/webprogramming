import "./Result.scss";

export default function Result({ args, operation, result, onClick }) {
  return (
    <div className="table-wrapper">
      <h1>Результат</h1>
      <table className="result-table">
        <thead>
          <tr>
            <th>Argument 1</th>
            <th>Argument 2</th>
            <th>Operation</th>
            <th>Result</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{args[0]}</td>
            <td>{args[1]}</td>
            <td>{operation}</td>
            <td>{result}</td>
          </tr>
        </tbody>
      </table>
      <img src="images/img.jpg" alt="" />
      <button onClick={onClick}>Назад</button>
    </div>
  );
}
