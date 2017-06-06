import java.util.*;
import java.lang.*;

class Tuple{
	int val;
	int xpos;
	int ypos;
}

class Matrix{
	public int[][] matrix;
	public int score;
	Matrix(){
		matrix = new int[10][10];
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				matrix[i][j]=0;
			}
		}
		score=0;
	}
	
	// print function is used to print the matrix

	void print(int[][] matrix,int size){
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(matrix[i][j]==-1){
					System.out.print("X");
				}
				else if(matrix[i][j]==1){
					System.out.print("O");
				}
				else{
					System.out.print(" ");
				}
				if(j!=size-1){
					System.out.print("|");
				}
				else{
					System.out.println("");
				}
			}
			for(int j=0;j<size;j++){
				System.out.print("- ");
			}
			System.out.println("");
		}
	}
	
	// full function tells whether matrix is has any empty spaces or not . If no empty space then return true else false.

	boolean full(int[][] matrix,int size){
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(matrix[i][j]==0){
					return false;
				}
			}
		}
		return true;
	}
	
	// check function checks whether there is n consecutive marks of one kind i.e. whether a person has won.

	boolean check(int[][] matrix,int size,int r,int c,int val){
		//check for row
		int flag=0;
		for(int i=0;i<size;i++){
			if(matrix[r][i]!=val){
				flag=1;
				break;
			}
		}
		if(flag==0){return true;}

		//check for column
		flag=0;
		for(int i=0;i<size;i++){
			if(matrix[i][c]!=val){
				flag=1;
				break;
			}
		}
		if(flag==0){return true;}

		//check for main diagonal
		flag=0;
		for(int i=0;i<size;i++){
			if(matrix[i][i]!=val){
				flag=1;
				break;
			}
		}
		if(flag==0){return true;}

		//check for other diagonal
		flag=0;
		for(int i=0;i<size;i++){
			if(matrix[i][size-i-1]!=val){
				flag=1;
				break;
			}
		}
		if(flag==0){return true;}
		return false;
	}
	
	// This is the decision tree which uses minimax algorithm and alpha beta pruning to take a decision.

	Tuple decisionTree(int[][] matrix,int size,int alpha,int beta,int turn,int depth){
		int[][] tempMatrix = new int[10][10];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				tempMatrix[i][j]=matrix[i][j];
				//System.out.print(tempMatrix[i][j] + " ");
			}
			//System.out.println("");
		}
		boolean d;
		int talpha = alpha;
		int tbeta = beta;
		Tuple temp = new Tuple();
		Tuple main_tuple = new Tuple();
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(tempMatrix[i][j]==0){
					if(talpha>=tbeta){
						return main_tuple;
					}
					tempMatrix[i][j]=turn;
					d = check(tempMatrix,size,i,j,turn);
					if(d){
						if(turn==1){
							temp.val = turn*10;
							temp.xpos = i;
							temp.ypos = j;
						}
						else{
							temp.val = turn*10;
							temp.xpos = i;
							temp.ypos = j;
						}
						return temp;
					}
					else if(full(tempMatrix,size)){
						if(turn==1){
							temp.val = 0;
							temp.xpos = i;
							temp.ypos = j;
						}
						else{
							temp.val = 0;
							temp.xpos = i;
							temp.ypos = j;
						}
						return temp;
					}
					else{
						temp = decisionTree(tempMatrix,size,talpha,tbeta,-1*turn,depth+1);
					}
					if(turn==1){
						if(temp.val>=talpha){
							talpha = temp.val;
							main_tuple.xpos = i;
							main_tuple.ypos = j;
							main_tuple.val = talpha;
						}
					}
					else{
						if(temp.val<=tbeta){
							tbeta = temp.val;
							main_tuple.val = tbeta;
							main_tuple.xpos = i;
							main_tuple.ypos = j;
						}
					}
					tempMatrix[i][j]=0;
					if(depth==1){
						System.out.println(i + " " + j + " " + main_tuple.xpos + " " + main_tuple.ypos);
					}
				}
			}
		}
		return main_tuple;
	}
	
	// computes where AI should place its mark

	void turnAI(int[][] matrix,int size,int[] result){
		int[][] tempMatrix = new int[10][10];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				tempMatrix[i][j]=matrix[i][j];
			}
		}
		Tuple ans = new Tuple();
		ans = decisionTree(tempMatrix,size,-1000000,1000000,1,1);
		if(matrix[ans.xpos][ans.ypos]!=0){
			System.out.println("There is Error");
		}
		matrix[ans.xpos][ans.ypos]=1;
		print(matrix,size);
		boolean d = check(matrix,size,ans.xpos,ans.ypos,1);
		if(d){
			System.out.println("Computer wins");
			result[0]=1;
			return;
		}
		turnuser(matrix,size,result);
	}
	
	// user function

	void turnuser(int[][] matrix,int size,int[] result){
		System.out.println("Enter your move");
		Scanner sc = new Scanner(System.in);
		int x = sc.nextInt();
		int y = sc.nextInt();
		while(matrix[x][y]!=0){
			System.out.println("Enter Again");
			x = sc.nextInt();
			y = sc.nextInt();
		}
		matrix[x][y]=-1;
		print(matrix,size);
		boolean d = check(matrix,size,x,y,-1);
		if(d){System.out.println("User Wins");result[0]=1;return;}
		d = full(matrix,size);
		if(d){System.out.println("Game Drawn");result[0]=1;return;}
		turnAI(matrix,size,result);
	}
}

class Tic_Tac_Toe{
	public static void main(String[] args) {
		System.out.println("Enter size of matrix");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		Matrix userMatrix = new Matrix();
		int[] result= new int[1];
		result[0]=-1;
		while(result[0]!=1){
			userMatrix.turnuser(userMatrix.matrix,n,result);
		}
	}
}
