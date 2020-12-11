library("ecp");

function(dataPointsArray) {
  dataPoints <- eval(parse(text=dataPointsArray))
  Xnorm <- matrix(dataPoints, ncol = 1);
  changePoints <- e.divisive(Xnorm, R = 499, alpha = 2);
  changePoints$estimates;
}