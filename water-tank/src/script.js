const CELL         = 36;
const PAD          = 4;
const COLOR_BLOCK  = '#f5e100';  
const COLOR_WATER  = '#5bc8f5'; 
const COLOR_EMPTY  = '#ffffff'; 
const COLOR_BORDER = '#aaaaaa';

function trap(height) {
  const n        = height.length;
  const leftMax  = new Array(n).fill(0);
  const rightMax = new Array(n).fill(0);

  leftMax[0] = height[0];
  for (let i = 1; i < n; i++)
    leftMax[i] = Math.max(leftMax[i - 1], height[i]);

  rightMax[n - 1] = height[n - 1];
  for (let i = n - 2; i >= 0; i--)
    rightMax[i] = Math.max(rightMax[i + 1], height[i]);

  let water = 0;
  const waterPerCol = new Array(n).fill(0);
  for (let i = 0; i < n; i++) {
    waterPerCol[i] = Math.min(leftMax[i], rightMax[i]) - height[i];
    water += waterPerCol[i];
  }

  return { water, waterPerCol };
}

function drawSVG(svgEl, heights, waterPerCol, mode) {
  const n    = heights.length;
  const maxH = Math.max(...heights, 1);
  const ROWS = maxH;
  const W    = PAD * 2 + n * CELL;
  const H    = PAD * 2 + ROWS * CELL;

  svgEl.setAttribute('width',   W);
  svgEl.setAttribute('height',  H);
  svgEl.setAttribute('viewBox', `0 0 ${W} ${H}`);
  svgEl.innerHTML = '';

  for (let col = 0; col < n; col++) {
    const blockH   = heights[col];
    const waterH   = waterPerCol[col];
    const waterTop = blockH + waterH;

    for (let row = 0; row < ROWS; row++) {
      const level = ROWS - row; 

      let fill;

      if (mode === 'input') {
        if (level <= blockH) {
          fill = COLOR_BLOCK;
        } else if (level <= waterTop) {
          fill = COLOR_WATER;
        } else {
          fill = COLOR_EMPTY;
        }
      } else {
        if (waterH > 0 && level > blockH && level <= waterTop) {
          fill = COLOR_WATER;
        } else {
          fill = COLOR_EMPTY;
        }
      }

      const rect = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
      rect.setAttribute('x',            PAD + col * CELL);
      rect.setAttribute('y',            PAD + row * CELL);
      rect.setAttribute('width',        CELL);
      rect.setAttribute('height',       CELL);
      rect.setAttribute('fill',         fill);
      rect.setAttribute('stroke',       COLOR_BORDER);
      rect.setAttribute('stroke-width', '1');
      svgEl.appendChild(rect);
    }
  }
}

function render() {
  const errEl  = document.getElementById('errorMsg');
  errEl.style.display = 'none';

  const raw     = document.getElementById('inputArray').value.trim();
  const parts   = raw.split(',');
  const heights = parts.map(Number);

  if (heights.some(isNaN)) {
    errEl.textContent = 'Enter comma-separated non-negative integers';
    errEl.style.display = 'block';
    document.getElementById('inputBlock').style.display  = 'none';
    document.getElementById('outputBlock').style.display = 'none';
    return;
  }

  const { water, waterPerCol } = trap(heights);

  document.getElementById('inputBlock').style.display = 'block';
  document.getElementById('inputLabel').textContent   = `Input: [${heights.join(',')}]`;
  drawSVG(document.getElementById('inputSvg'), heights, waterPerCol, 'input');

  document.getElementById('outputBlock').style.display = 'block';
  document.getElementById('outputLabel').textContent   = `Output: ${water} Units`;
  drawSVG(document.getElementById('outputSvg'), heights, waterPerCol, 'output');
}

