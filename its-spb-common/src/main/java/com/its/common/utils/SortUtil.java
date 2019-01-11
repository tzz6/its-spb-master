package com.its.common.utils;

/**
 * 常用排序工具类
 *
 */
public class SortUtil {

	/** 1.选择排序： n-1趟，每一趟找出最小值的的下标，将其放在第n-1个位置 **/
	public static int[] selectSorter(int[] num) {
		int min;// 较小值的下标
		int temp;// 临时下标
		for (int i = 0; i < num.length - 1; i++) {
			min = i;
			for (int j = i + 1; j < num.length; j++) {
				if (num[min] > num[j]) {
					min = j;
				}
			}
			temp = num[i];
			num[i] = num[min];
			num[min] = temp;
		}
		return num;
	}

	/*** 2.冒泡排序：依次比较相邻的两个数，将小数放在前面，大数放在后面 */
	public static int[] bubbleSorter(int[] num) {
		int temp;// 临时下标
		for (int i = 0; i < num.length - 1; i++) {
			for (int j = 0; j < num.length - i - 1; j++) {
				if (num[j] > num[j + 1]) {
					temp = num[j];
					num[j] = num[j + 1];
					num[j + 1] = temp;
				}
			}
		}
		return num;
	}

	/**
	3.快速排序
	1)设置两个变量I、J，排序开始的时候：I=0，J=N-1； 
	　　2）以第一个数组元素作为关键数据，赋值给key，即 key=A[0]； 
	　　3）从J开始向前搜索，即由后开始向前搜索（J=J-1即J--），找到第一个小于key的值A[j]，A[j]与A[i]交换； 
	　　4）从I开始向后搜索，即由前开始向后搜索（I=I+1即I++），找到第一个大于key的A[i]，A[i]与A[j]交换； 
	5）重复第3、4、5步，直到 I=J； (3,4步是在程序中没找到时候j=j-1，i=i+1，直至找到为止。找到并交换的时候i， j指针位置不变。另外当i=j这过程一定正好是i+或j-完成的最后另循环结束。）
	示例：待排序的数组A的值分别是：（初始关键数据：key=49） 注意关键key永远不变，永远是和key进行比较，无论在什么位子，最后的目的就是把key放在中间，小的放前面大的放后面。 
	　　 A[0]	A[1]	A[2]	A[3]	A[4]	A[5]	A[6]
	    49	    38	    65	    97	     76	     13	     27
	　  进行第一次交换后：27 38 65 97 76 13 49 
	　　( 按照算法的第三步从后面开始找） 
	　　进行第二次交换后：27 38 49 97 76 13 65 
	　　( 按照算法的第四步从前面开始找>key的值，65>49,两者交换，此时：I=3 ) 
	　　进行第三次交换后：27 38 13 97 76 49 65 
	　　( 按照算法的第五步将又一次执行算法的第三步从后开始找 
	　　进行第四次交换后：27 38 13 49 76 97 65
	**/
	public static int[] quickSorter(int[] num, int left, int rigth) {
		int i, j, key;
		if (left < rigth) {
			i = left;
			j = rigth;
			key = num[i];
			while (i < j) {
				while (i < j && num[j] > key) {
					j--; /* 从右向左找第一个小于x的数 */
				}
				if (i < j) {
					num[i++] = num[j];
				}
				while (i < j && num[i] < key) {
					i++; /* 从左向右找第一个大于x的数 */
				}
				if (i < j) {
					num[j--] = num[i];
				}

			}

			// for (int k = 0; k < num.length - 1; k++) {
			// System.out.print(num[k] + ",");
			// }
			// System.out.println(num[num.length - 1]);

			num[i] = key;
			/* 递归调用 */
			quickSorter(num, left, i - 1);// 递归左
			quickSorter(num, i + 1, rigth);// 递归右
		}

		return num;
	}

	// 4.插入排序
	// 1. 从第一个元素开始，该元素可以认为已经被排序
	// 2. 取出下一个元素，在已经排序的元素序列中从后向前扫描
	// 3. 如果该元素（已排序）大于新元素，将该元素移到下一位置
	// 4. 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置
	// 5. 将新元素插入到下一位置中
	// 6. 重复步骤2
	// 如果比较操作的代价比交换操作大的话，可以采用二分查找法来减少比较操作的数目。该算法可以认为是插入排序的一个变种，称为二分查找排序。
	public static int[] insetSorter(int[] num) {
		for (int i = 1; i < num.length; i++) {
			int temp = num[i];
			int j;
			for (j = i; j > 0 && temp < num[j - 1]; j--) {
				num[j] = num[j - 1];
			}
			num[j] = temp;
		}
		return num;
	}

	// 希尔排序
	public static int[] shellSorter(int[] num, int index) {
		int j, k; // 循环计数变量
		int Temp; // 暂存变量
		boolean Change; // 数据是否改变
		int DataLength; // 分割集合的间隔长度
		int Pointer; // 进行处理的位置
		DataLength = index / 2;
		// 初始集合间隔长度
		while (DataLength != 0) { // 数列仍可进行分割
			// 对各个集合进行处理
			for (j = DataLength; j < index; j++) {
				Change = false;
				Temp = num[j]; // 暂存Data[j]的值,待交换值时用
				Pointer = j - DataLength; // 计算进行处理的位置
				// 进行集合内数值的比较与交换值
				while (Temp < num[Pointer] && Pointer >= 0 && Pointer <= index) {
					num[Pointer + DataLength] = num[Pointer];
					// 计算下一个欲进行处理的位置
					Pointer = Pointer - DataLength;
					Change = true;
					if (Pointer < 0 || Pointer > index) {
						break;
					}
				}
				// 与最后的数值交换
				num[Pointer + DataLength] = Temp;

				if (Change) { // 打印目前排序结果
					 System.out.print("排序中: ");
					 for (k = 0; k < index; k++) {
					 System.out.printf("%3s ", num[k]);
					 }
					 System.out.println("");
				}
			}
			// 计算下次分割的间隔长度
			DataLength = DataLength / 2;
		}
		return num;
	}
	
	
	/**
     * 6.归并排序
     * 简介:将两个（或两个以上）有序表合并成一个新的有序表 即把待排序序列分为若干个子序列，每个子序列是有序的。然后再把有序子序列合并为整体有序序列
     * 时间复杂度为O(nlogn)
     * 稳定排序方式
     * @param nums 待排序数组
     * @return 输出有序数组
     */
    public static int[] mergeSort(int[] nums, int low, int high) {
        int mid = (low + high) / 2;
        if (low < high) {
            // 左边
        	mergeSort(nums, low, mid);
            // 右边
        	mergeSort(nums, mid + 1, high);
            // 左右归并
            merge(nums, low, mid, high);
        }
        return nums;
    }

    /**
     * 将数组中low到high位置的数进行排序
     * @param nums 待排序数组
     * @param low 待排的开始位置
     * @param mid 待排中间位置
     * @param high 待排结束位置
     */
	public static void merge(int[] nums, int low, int mid, int high) {
		int[] temp = new int[high - low + 1];
		int i = low;// 左指针
		int j = mid + 1;// 右指针
		int k = 0;

		// 把较小的数先移到新数组中
		while (i <= mid && j <= high) {
			if (nums[i] < nums[j]) {
				temp[k++] = nums[i++];
			} else {
				temp[k++] = nums[j++];
			}
		}

		// 把左边剩余的数移入数组
		while (i <= mid) {
			temp[k++] = nums[i++];
		}

		// 把右边边剩余的数移入数组
		while (j <= high) {
			temp[k++] = nums[j++];
		}

		// 把新数组中的数覆盖nums数组
		for (int k2 = 0; k2 < temp.length; k2++) {
			nums[k2 + low] = temp[k2];
		}
	}
	
	/** 7.堆排序 */
	public static int[] heapSort(int[] num) {
		int arrayLength = num.length;
		// 循环建堆
		for (int i = 0; i < arrayLength - 1; i++) {
			// 建堆
			buildMaxHeap(num, arrayLength - 1 - i);
			// 交换堆顶和最后一个元素
			swap(num, 0, arrayLength - 1 - i);
		}
		return num;
	}

	// 对data数组从0到lastIndex建大顶堆
	public static void buildMaxHeap(int[] data, int lastIndex) {
		// 从lastIndex处节点（最后一个节点）的父节点开始
		for (int i = (lastIndex - 1) / 2; i >= 0; i--) {
			// k保存正在判断的节点
			int j = i;
			// 如果当前k节点的子节点存在
			while (j * 2 + 1 <= lastIndex) {
				// k节点的左子节点的索引
				int biggerIndex = 2 * j + 1;
				// 如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在
				if (biggerIndex < lastIndex) {
					// 若果右子节点的值较大
					if (data[biggerIndex] < data[biggerIndex + 1]) {
						// biggerIndex总是记录较大子节点的索引
						biggerIndex++;
					}
				}
				// 如果k节点的值小于其较大的子节点的值
				if (data[j] < data[biggerIndex]) {
					// 交换他们
					swap(data, j, biggerIndex);
					// 将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值
					j = biggerIndex;
				} else {
					break;
				}
			}
		}
	}

	// 交换
	public static void swap(int[] data, int i, int j) {
		int tmp = data[i];
		data[i] = data[j];
		data[j] = tmp;
	}

}
