library("ecp");

eDivFromFile <- function(fileName, ncols) {
  dataPoints <- scan(fileName)
  mat <- matrix(dataPoints, ncol = ncols, byrow = TRUE)
  changePoints <- e.divisive(mat, R = 499, alpha = 2);
  changePoints$estimates;
}